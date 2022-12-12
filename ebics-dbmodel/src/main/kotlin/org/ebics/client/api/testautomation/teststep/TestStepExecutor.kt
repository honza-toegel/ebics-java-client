package org.ebics.client.api.testautomation.teststep

import org.ebics.client.api.testautomation.testexecution.TestExecutionContext

interface TestStepExecutor<D: TestStepDefinition, I: TestStepDataInput, O: TestStepDataOutput> {
    fun executeStep(testStepDefinition: D, testStepDataInput: I, testExecutionContext: TestExecutionContext): O
}