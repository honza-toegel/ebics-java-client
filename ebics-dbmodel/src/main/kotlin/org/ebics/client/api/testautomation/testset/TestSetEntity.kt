package org.ebics.client.api.testautomation.testset

import org.ebics.client.api.testautomation.testcase.TestCase
import org.ebics.client.api.testautomation.testset.scheduledexecution.TestSetScheduledExecutionEntity
import javax.persistence.*

@Entity
data class TestSetEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "testSet")
    val testCases: List<TestCase>,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "testSet")
    val scheduledExecutions: List<TestSetScheduledExecutionEntity>
)
