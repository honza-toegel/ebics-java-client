package org.ebics.client.api.testautomation.teststep.generate

import org.ebics.client.api.bankconnection.properties.BankConnectionPropertyEntity
import org.ebics.client.api.bankconnection.properties.BankConnectionPropertyService
import org.ebics.client.api.testautomation.testexecution.TestExecutionContext
import org.ebics.client.api.testautomation.teststep.TestStepExecutor
import org.ebics.client.api.uploadtemplates.FileTemplate
import org.ebics.client.api.uploadtemplates.FileTemplateService
import org.springframework.stereotype.Service

@Service
class GenerateFileFromTemplateTestStepExecutor(private val templateService: FileTemplateService, private val bankConnectionPropertyService: BankConnectionPropertyService) :
    TestStepExecutor<GenerateFileFromTemplateTestStepDefinition, GenerateFileFromTemplateTestStepDataInput, GenerateFileFromTemplateTestStepDataOutput> {
    override fun executeStep(
        testStepDefinition: GenerateFileFromTemplateTestStepDefinition,
        testStepDataInput: GenerateFileFromTemplateTestStepDataInput,
        testExecutionContext: TestExecutionContext
    ): GenerateFileFromTemplateTestStepDataOutput {
        val template = templateService.getTemplateById(testStepDefinition.templateId)
        val bankConnectionProperties = bankConnectionPropertyService.findAllByBankConnectionId(testExecutionContext.bankConnectionId)
        return GenerateFileFromTemplateTestStepDataOutput(replaceTemplatePlaceholdersWithRealValues(template, bankConnectionProperties).toByteArray())
    }

    private fun replaceTemplatePlaceholdersWithRealValues(template: FileTemplate, bankConnectionProperties: List<BankConnectionPropertyEntity>): String {
        return template.fileContentText
        TODO("Not yet implemented")
    }
}