package org.ebics.client.api.testautomation.teststep.generate

import org.ebics.client.api.bankconnection.properties.BankConnectionPropertyService
import org.ebics.client.api.security.AuthenticationContext
import org.ebics.client.api.testautomation.testexecution.TestExecutionContext
import org.ebics.client.api.testautomation.teststep.TestStepExecutor
import org.ebics.client.api.uploadtemplates.FileTemplateService
import org.springframework.stereotype.Service

@Service
class GenerateFileFromTemplateTestStepExecutor(
    private val templateService: FileTemplateService,
    private val bankConnectionPropertyService: BankConnectionPropertyService,
    private val templatePlaceholderReplacingService: TemplatePlaceholderReplacingService,
) :
    TestStepExecutor<GenerateFileFromTemplateTestStepDefinition, GenerateFileFromTemplateTestStepDataInput, GenerateFileFromTemplateTestStepDataOutput> {
    override fun executeStep(
        testStepDefinition: GenerateFileFromTemplateTestStepDefinition,
        testStepDataInput: GenerateFileFromTemplateTestStepDataInput,
        testExecutionContext: TestExecutionContext
    ): GenerateFileFromTemplateTestStepDataOutput {
        val template = templateService.getTemplateById(testStepDefinition.templateId)
        val bankConnectionProperties =
            bankConnectionPropertyService.findAllByBankConnectionId(testExecutionContext.bankConnectionId)
        val userId = try {
            AuthenticationContext.fromSecurityContext().name
        } catch (ex: IllegalAccessException) {
            //If there is no user from the context, then we must be triggered by scheduler
            "scheduler"
        }
        val generatedFileContent = templatePlaceholderReplacingService.replaceTemplatePlaceholdersWithValues(
            userId,
            template,
            bankConnectionProperties
        )
        return GenerateFileFromTemplateTestStepDataOutput(generatedFileContent)
    }
}