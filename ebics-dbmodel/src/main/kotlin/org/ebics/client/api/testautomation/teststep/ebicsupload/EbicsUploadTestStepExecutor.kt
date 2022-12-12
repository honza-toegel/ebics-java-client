package org.ebics.client.api.testautomation.teststep.ebicsupload

import org.ebics.client.api.testautomation.testexecution.TestExecutionContext
import org.ebics.client.api.testautomation.teststep.TestStepExecutor
import org.springframework.stereotype.Service

@Service
class EbicsUploadTestStepExecutor(
    private val fileUploadH005: org.ebics.client.filetransfer.h005.FileUpload,
    private val fileUploadH004: org.ebics.client.filetransfer.h004.FileUpload
) : TestStepExecutor<EbicsUploadTestStepDefinition, EbicsUploadTestStepDataInput, EbicsUploadTestStepDataOutput> {
    override fun executeStep(
        testStepDefinition: EbicsUploadTestStepDefinition,
        testStepDataInput: EbicsUploadTestStepDataInput,
        testExecutionContext: TestExecutionContext
    ): EbicsUploadTestStepDataOutput {
        val response = fileUploadH004.sendFile(
            TODO("session"),
            TODO("traceSession"),
            testStepDataInput.inputFileContent,
            testStepDefinition.orderType004
        )
        return EbicsUploadTestStepDataOutput(response.orderNumber, response.transactionId)
    }
}