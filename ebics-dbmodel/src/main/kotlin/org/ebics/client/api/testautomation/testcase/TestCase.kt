package org.ebics.client.api.testautomation.testcase

import org.ebics.client.api.testautomation.testset.TestSetEntity
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class TestCase (
    @Id
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @ManyToOne(optional = false)
    val testSet: TestSetEntity
)

