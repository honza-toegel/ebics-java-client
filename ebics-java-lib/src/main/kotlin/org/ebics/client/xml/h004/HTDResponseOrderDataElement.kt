/*
 * Copyright (c) 1990-2012 kopiLeft Development SARL, Bizerte, Tunisia
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id$
 */
package org.ebics.client.xml.h004

import org.ebics.client.exception.EbicsException
import org.ebics.client.interfaces.ContentFactory
import org.ebics.client.order.AuthorisationLevel
import org.ebics.client.order.EbicsAdminOrderType
import org.ebics.client.order.h004.OrderType
import org.ebics.client.order.h004.TransferType
import org.ebics.schema.h004.AuthOrderInfoType
import org.ebics.schema.h004.HTDReponseOrderDataType
import org.ebics.schema.h004.HTDResponseOrderDataDocument

/**
 * The `HPBResponseOrderDataElement` contains the public bank
 * keys in encrypted mode. The user should decrypt with his encryption
 * key to have the bank public keys.
 *
 * @author hachani
 */
/**
 * Creates a new `HPBResponseOrderDataElement` from a given
 * content factory.
 * @param factory the content factory.
 */
class HTDResponseOrderDataElement(factory: ContentFactory) : DefaultResponseElement(factory, "HTDData") {

    @Throws(EbicsException::class)
    override fun build() {
        parse(factory)
        response = (document as HTDResponseOrderDataDocument).htdResponseOrderData
    }

    override val name: String = "HTDData.xml"

    /**
     * Return list of order-types available for user
     * with all details (description, number of signatures, rights,..)
     */
    fun getOrderTypes(): List<OrderType> {
        with(response) {
            return userInfo.permissionArray.flatMap { permissions ->
                //Lets find for each user permission the referred BTF type
                val authorisationLevel =
                    permissions.authorisationLevel?.let { AuthorisationLevel.valueOf(it.toString()) }
                permissions.orderTypes.mapNotNull { orderType ->
                    partnerInfo.orderInfoArray.find { orderInfo -> orderInfo.orderType == orderType }
                        ?.let { it to authorisationLevel }
                }
            }.map {
                createOrderType(it.first, it.second)
            }
        }
    }


    private fun createOrderType(orderInfo: AuthOrderInfoType, authLevel: AuthorisationLevel?): OrderType =
        OrderType(
            recognizeAdminOrderType(orderInfo),
            orderInfo.orderType,
            orderInfo.transferType?.let { tt -> TransferType.valueOf(tt.toString()) },
            orderInfo.description,
            authLevel,
            orderInfo.numSigRequired
        )


    private fun recognizeAdminOrderType(orderInfo: AuthOrderInfoType): EbicsAdminOrderType? {
        return try {
            EbicsAdminOrderType.valueOf(orderInfo.orderType)
        } catch (e: Exception) {
            when (orderInfo.transferType?.toString()) {
                TransferType.Upload.name -> EbicsAdminOrderType.UPL
                TransferType.Download.name -> EbicsAdminOrderType.DNL
                else -> null
            }
        }
    }

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------
    lateinit var response: HTDReponseOrderDataType

    companion object {
        private const val serialVersionUID = -1305363936881364049L
    }
}