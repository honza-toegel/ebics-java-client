package org.ebics.client.api.testautomation.testcase

import org.ebics.client.api.testautomation.testexecution.TestExecutionContext
import org.ebics.client.api.testautomation.teststep.ebicsupload.EbicsUploadTestStepDataInput
import org.ebics.client.api.testautomation.teststep.ebicsupload.EbicsUploadTestStepExecutor
import org.ebics.client.api.testautomation.teststep.generate.GenerateFileFromTemplateTestStepDataInput
import org.ebics.client.api.testautomation.teststep.generate.GenerateFileFromTemplateTestStepExecutor
import org.springframework.stereotype.Service

/**
 * This test case utilizes following test steps:
 * - GenerateFileFromTemplate
 * - EbicsFileUploadTestStep
 */
@Service
class EbicsFileUploadTestCaseExecutor(
    private val generateFileFromTemplateTestStepExecutor: GenerateFileFromTemplateTestStepExecutor,
    private val ebicsUploadTestStepExecutor: EbicsUploadTestStepExecutor
) : GenericTestCaseExecutor<EbicsFileUploadTestCase> {
    override fun executeTestCase(testCase: EbicsFileUploadTestCase, testExecutionContext: TestExecutionContext) {
        val generateStepOutput = generateFileFromTemplateTestStepExecutor.executeStep(
            testCase.generateFileFromTemplateTestStep,
            GenerateFileFromTemplateTestStepDataInput(),
            testExecutionContext
        )

        val uploadStepInput = EbicsUploadTestStepDataInput(generateStepOutput.generateFileContent)

        val uploadStepOutput = ebicsUploadTestStepExecutor.executeStep(
            testCase.ebicsUploadTestStep,
            uploadStepInput,
            testExecutionContext
        )
    }
}