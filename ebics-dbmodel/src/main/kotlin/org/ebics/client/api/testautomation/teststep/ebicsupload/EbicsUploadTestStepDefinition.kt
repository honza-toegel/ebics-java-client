package org.ebics.client.api.testautomation.teststep.ebicsupload

import org.ebics.client.api.testautomation.teststep.TestStepDefinition
import javax.persistence.Embeddable
import javax.persistence.Embedded

@Embeddable
class EbicsUploadTestStepDefinition(
    @Embedded
    val orderType004: org.ebics.client.order.h004.EbicsUploadOrder,
    @Embedded
    val orderType005: org.ebics.client.order.h005.EbicsUploadOrder
) : TestStepDefinition {

}