package org.ebics.client.api.trace.orderType

import org.ebics.client.order.EbicsAdminOrderType
import org.ebics.client.order.IOrderTypeDefinition
import org.ebics.client.order.IOrderTypeDefinition25
import org.ebics.client.order.IOrderTypeDefinition30
import javax.persistence.Embeddable
import javax.persistence.Embedded

@Embeddable
data class OrderTypeDefinition(
    //For H002-H005
    val adminOrderType: EbicsAdminOrderType,

    //For H005 order types
    @Embedded
    val ebicsServiceType: EbicsService? = null,
    //For H002-H004 order types
    val businessOrderType: String? = null,
) {
    companion object {
        fun fromOrderType(orderType: IOrderTypeDefinition): OrderTypeDefinition {
            return when {
                (orderType.javaClass.isAssignableFrom(IOrderTypeDefinition25::class.java)) ->
                   OrderTypeDefinition(orderType.adminOrderType, businessOrderType = (orderType as IOrderTypeDefinition25).businessOrderType)
                (orderType.javaClass.isAssignableFrom(IOrderTypeDefinition30::class.java)) -> {
                    val ebicsService = (orderType as IOrderTypeDefinition30).service?.let { service -> EbicsService.fromEbicsService(service)}
                    OrderTypeDefinition(orderType.adminOrderType, ebicsServiceType = ebicsService)
                }
                else ->
                    throw IllegalArgumentException("Can't instantiate OrderTypeDefinition, Unsupported ordertype class: '${orderType.javaClass.name}'")
            }
        }
    }
}
