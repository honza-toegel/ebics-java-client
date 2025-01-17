package org.ebics.client.api.bankconnection.properties

import com.fasterxml.jackson.annotation.JsonIgnore
import org.ebics.client.api.bankconnection.BankConnectionEntity
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(name = "uniqueKeyToBankConnection", columnNames = ["key", "bankConnection_id"])])
data class BankConnectionPropertyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(length = 100)
    val key: String,

    @Column(length = 500)
    val value: String,

    @JsonIgnore
    @ManyToOne(optional = false)
    val bankConnection: BankConnectionEntity,
) {
    companion object {
        fun from(bankConnectionEntity: BankConnectionEntity, bankConnectionPropertyUpdateRequest: BankConnectionPropertyUpdateRequest): BankConnectionPropertyEntity {
            return BankConnectionPropertyEntity(bankConnectionPropertyUpdateRequest.id, bankConnectionPropertyUpdateRequest.key, bankConnectionPropertyUpdateRequest.value, bankConnectionEntity)
        }
    }
}
