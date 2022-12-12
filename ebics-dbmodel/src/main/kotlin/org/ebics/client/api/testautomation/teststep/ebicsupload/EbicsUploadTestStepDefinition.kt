package org.ebics.client.api.testautomation.teststep.ebicsupload

import org.ebics.client.api.testautomation.teststep.TestStepDefinition

class EbicsUploadTestStepDefinition(
    val orderType004: org.ebics.client.order.h004.EbicsUploadOrder,
    val orderType005: org.ebics.client.order.h005.EbicsUploadOrder
) : TestStepDefinition {

}