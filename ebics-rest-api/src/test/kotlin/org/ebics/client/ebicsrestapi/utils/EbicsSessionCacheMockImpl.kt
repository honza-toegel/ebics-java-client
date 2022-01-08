package org.ebics.client.ebicsrestapi.utils

import org.ebics.client.api.EbicsConfiguration
import org.ebics.client.api.user.permission.BankConnectionAccessType
import org.ebics.client.ebicsrestapi.MockUser
import org.ebics.client.ebicsrestapi.bankconnection.UserIdPass
import org.ebics.client.ebicsrestapi.bankconnection.session.IEbicsSessionCache
import org.ebics.client.model.EbicsSession
import org.ebics.client.model.Product

class EbicsSessionCacheMockImpl(private val configuration: EbicsConfiguration, private val product: Product) : IEbicsSessionCache {
    override fun getSession(
        userIdPass: UserIdPass,
        bankKeysRequired: Boolean,
        accessType: BankConnectionAccessType
    ): EbicsSession {
        val user = MockUser.createMockUser(1, true)
        return EbicsSession(user, configuration, product, user.keyStore!!.toUserCertMgr("pass1"), null)
    }
}