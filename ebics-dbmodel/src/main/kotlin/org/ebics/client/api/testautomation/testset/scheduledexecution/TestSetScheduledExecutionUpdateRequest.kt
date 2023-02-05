package org.ebics.client.api.testautomation.testset.scheduledexecution

class TestSetScheduledExecutionUpdateRequest (
    val testSetId: Long,
    val cronType: TestSetSchedulerCronType,
    val disabled: Boolean
)