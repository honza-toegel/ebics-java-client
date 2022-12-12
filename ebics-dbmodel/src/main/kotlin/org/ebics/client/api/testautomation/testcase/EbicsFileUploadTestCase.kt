package org.ebics.client.api.testautomation.testcase

import org.ebics.client.api.testautomation.teststep.ebicsupload.EbicsUploadTestStepDefinition
import org.ebics.client.api.testautomation.teststep.generate.GenerateFileFromTemplateTestStepDefinition

class EbicsFileUploadTestCase(
    val generateFileFromTemplateTestStep: GenerateFileFromTemplateTestStepDefinition,
    val ebicsUploadTestStep: EbicsUploadTestStepDefinition
) : TestCase