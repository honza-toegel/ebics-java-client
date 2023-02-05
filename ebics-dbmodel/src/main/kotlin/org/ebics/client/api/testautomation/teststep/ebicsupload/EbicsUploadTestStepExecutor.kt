package org.ebics.client.api.testautomation.teststep.ebicsupload

import org.ebics.client.api.testautomation.testexecution.TestExecutionContext

interface EbicsUploadTestStepExecutor {
    fun executeStep(
        testStepDefinition: EbicsUploadTestStepDefinition,
        testStepDataInput: EbicsUploadTestStepDataInput,
        testExecutionContext: TestExecutionContext
    ): EbicsUploadTestStepDataOutput
}