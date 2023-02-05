package org.ebics.client.api.testautomation.testcase

import org.ebics.client.api.testautomation.testset.TestSetEntity
import org.ebics.client.api.testautomation.teststep.ebicsupload.EbicsUploadTestStepDefinition
import org.ebics.client.api.testautomation.teststep.generate.GenerateFileFromTemplateTestStepDefinition
import javax.persistence.Embedded
import javax.persistence.Entity

@Entity
class EbicsFileUploadTestCase(
    id: Long? = null,
    testSet: TestSetEntity,
    @Embedded
    val generateFileFromTemplateTestStep: GenerateFileFromTemplateTestStepDefinition,
    @Embedded
    val ebicsUploadTestStep: EbicsUploadTestStepDefinition
) : TestCase(id, testSet)