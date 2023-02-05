package org.ebics.client.api.testautomation.testset.scheduledexecution

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TestSetScheduledExecutionRepository: JpaRepository<TestSetScheduledExecutionEntity, Long>