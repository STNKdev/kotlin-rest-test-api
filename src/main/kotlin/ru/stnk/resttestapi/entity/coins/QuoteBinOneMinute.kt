package ru.stnk.resttestapi.entity.coins

import com.fasterxml.jackson.annotation.JsonIgnore
import ru.stnk.resttestapi.entity.AuditModel
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
class QuoteBinOneMinute : AuditModel, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long? = null

    @Column
    var symbolName: String? = null

    @Column
    var bidPrice: Double? = null

    @Column
    var askPrice: Double? = null

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    var timestamp: Date? = null

    constructor() {}

    constructor(symbolName: String, bidPrice: Double?, askPrice: Double?, timestamp: Date) {
        this.symbolName = symbolName
        this.bidPrice = bidPrice
        this.askPrice = askPrice
        this.timestamp = timestamp
    }
}
