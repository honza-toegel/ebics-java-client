package org.ebics.client.api.testautomation.testset.scheduledexecution

import org.ebics.client.api.getById
import org.ebics.client.api.testautomation.testset.TestSetRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class TestSetScheduledExecutionService(
    private val testSetScheduledExecutor: TestSetScheduledExecutor,
    private val testSetScheduledExecutionRepository: TestSetScheduledExecutionRepository,
    private val testSetRepository: TestSetRepository,
) {
    //@Scheduled(cron = "\${testautomation.customScheduler1.cron}")
    @Scheduled(cron = "*/1 * * * * *")
    fun customScheduler1() {
        executeAllJobsForCron(TestSetSchedulerCronType.Custom1)
    }

    //@Scheduled(cron = "\${testautomation.customScheduler2.cron}")
    @Scheduled(cron = "*/5 * * * * *")
    fun customScheduler2() {
        executeAllJobsForCron(TestSetSchedulerCronType.Custom2)
    }

    private fun executeAllJobsForCron(cronType: TestSetSchedulerCronType) {
        testSetScheduledExecutionRepository.findAll().filter { job -> job.cronType == cronType }.forEach {
            job -> testSetScheduledExecutor.executeTestSet(job)
        }
    }

    fun addTestSetScheduledExecution(jobDefinition: TestSetScheduledExecutionUpdateRequest): Long {
        val testSet = testSetRepository.getById(jobDefinition.testSetId, "testset")
        val executionEntity = TestSetScheduledExecutionEntity(null, testSet, jobDefinition.cronType, jobDefinition.disabled)
        testSetScheduledExecutionRepository.save(executionEntity)
        return executionEntity.id!!
    }

    fun updateTestSetScheduledExecution(jobId: Long, jobDefinition: TestSetScheduledExecutionUpdateRequest) {
        val testSet = testSetRepository.getById(jobDefinition.testSetId, "testset")
        val executionEntity = TestSetScheduledExecutionEntity(jobId, testSet, jobDefinition.cronType, jobDefinition.disabled)
        testSetScheduledExecutionRepository.save(executionEntity)
    }

    fun removeTestSetScheduledExecution(jobId: Long) {
        testSetScheduledExecutionRepository.deleteById(jobId)
    }
}