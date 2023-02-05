package org.ebics.client.api.testautomation.testset.scheduledexecution

import org.ebics.client.api.testautomation.testset.TestSetRepository
import org.ebics.client.api.testautomation.testset.TestSetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@Lazy
@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = ["org.ebics.client.api.*"])
@EntityScan(basePackages = ["org.ebics.client.api.*"])
open class TaskSchedulerContext(
    @Autowired private val testSetScheduledExecutionRepository: TestSetScheduledExecutionRepository,
    @Autowired private val testSetRepository: TestSetRepository
) {
    @Bean
    open fun testSetScheduledExecutor() = TestSetScheduledExecutor()

    @Bean
    open fun executionService() = TestSetScheduledExecutionService(
        testSetScheduledExecutor(),
        testSetScheduledExecutionRepository,
        testSetRepository
    )

    @Bean
    open fun testSetService() = TestSetService(testSetRepository)
}