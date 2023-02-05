package org.ebics.client.api.testautomation.testset.scheduledexecution

import org.ebics.client.api.testautomation.testset.TestSetEntity
import javax.persistence.*

@Entity
class TestSetScheduledExecutionEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @ManyToOne(optional = false)
    val testSet: TestSetEntity,
    override val cronType: TestSetSchedulerCronType,
    val disabled: Boolean
) : JobDefinition