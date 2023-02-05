package org.ebics.client.api.testautomation.testset.scheduledexecution

interface JobDefinition {
    val id: Long?
    val cronType: TestSetSchedulerCronType
}