package org.ebics.client.api.testautomation.teststep.generate

import org.ebics.client.api.testautomation.teststep.TestStepDefinition
import javax.persistence.Embeddable

@Embeddable
class GenerateFileFromTemplateTestStepDefinition(
    val templateId: Long,
) : TestStepDefinition