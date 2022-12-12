package org.ebics.client.api.testautomation.testcase

import org.ebics.client.api.testautomation.testexecution.TestExecutionContext

interface GenericTestCaseExecutor<T: TestCase> {
    fun executeTestCase(testCase: T, testExecutionContext: TestExecutionContext)
}