package ru.stnk.resttestapi.entity.coins

import com.fasterxml.jackson.annotation.JsonIgnore
import ru.stnk.resttestapi.entity.AuditModel
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "instrument")
class InstrumentEntity : AuditModel, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    var id: Long? = null

    @Column(name = "symbol_name")
    var symbolName: String? = null

    @Column(name = "high_price")
    var highPrice: Double? = null

    @Column(name = "low_price")
    var lowPrice: Double? = null

    @Column(name = "last_price")
    var lastPrice: Double? = null

    @Column(name = "bid_price")
    var bidPrice: Double? = null

    @Column(name = "ask_price")
    var askPrice: Double? = null

    constructor() {}

    constructor(symbolName: String, highPrice: Double?, lowPrice: Double?, lastPrice: Double?, bidPrice: Double?, askPrice: Double?) {
        this.symbolName = symbolName
        this.highPrice = highPrice
        this.lowPrice = lowPrice
        this.lastPrice = lastPrice
        this.bidPrice = bidPrice
        this.askPrice = askPrice
    }
}
