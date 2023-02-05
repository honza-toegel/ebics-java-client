package org.ebics.client.api.testautomation.testset.scheduledexecution

import org.ebics.client.api.testautomation.testset.TestSetService
import org.ebics.client.api.testautomation.testset.TestSetUpdateRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

class TestJobDef(override val id: Long?, override val cronType: TestSetSchedulerCronType) : JobDefinition

@ExtendWith(SpringExtension::class)
@DataJpaTest
@ContextConfiguration(classes = [TaskSchedulerContext::class])
class TestSetScheduledExecutionServiceTest(
    @Autowired private val scheduledExecutionService: TestSetScheduledExecutionService,
    @Autowired private val testSetService: TestSetService
) {
    @Test
    fun testScheduleNewJob() {
        //Every ten seconds
        val testSetId = testSetService.addTestSet(TestSetUpdateRequest("test"))
        val testExecution1 = TestSetScheduledExecutionUpdateRequest(testSetId, TestSetSchedulerCronType.Custom1, false)
        val testExecution2 = TestSetScheduledExecutionUpdateRequest(testSetId, TestSetSchedulerCronType.Custom2, false)
        val testExecution3 = TestSetScheduledExecutionUpdateRequest(testSetId, TestSetSchedulerCronType.Custom2, false)
        scheduledExecutionService.addTestSetScheduledExecution(testExecution1)
        scheduledExecutionService.addTestSetScheduledExecution(testExecution2)
        val id3 = scheduledExecutionService.addTestSetScheduledExecution(testExecution3)
        Thread.sleep(3000)
        scheduledExecutionService.removeTestSetScheduledExecution(id3)
        Thread.sleep(10000)
    }
}