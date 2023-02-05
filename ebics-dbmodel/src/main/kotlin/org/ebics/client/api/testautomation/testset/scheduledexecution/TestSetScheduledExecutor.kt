package org.ebics.client.api.testautomation.testset.scheduledexecution

import org.springframework.stereotype.Service

@Service
class TestSetScheduledExecutor  {
    fun executeTestSet(jobDefinition: JobDefinition) {
        println(jobDefinition.id)
    }
}