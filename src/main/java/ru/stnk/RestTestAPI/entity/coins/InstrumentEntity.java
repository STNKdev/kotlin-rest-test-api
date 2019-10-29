package ru.stnk.RestTestAPI.entity.coins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.stnk.RestTestAPI.entity.AuditModel;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "INSTRUMENT")
public class InstrumentEntity extends AuditModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = "symbol_name")
    private String symbolName;

    @Column(name = "high_price")
    private Double highPrice;

    @Column(name = "low_price")
    private Double lowPrice;

    @Column(name = "last_price")
    private Double lastPrice;

    @Column(name = "bid_price")
    private Double bidPrice;

    @Column(name = "ask_price")
    private Double askPrice;

    public InstrumentEntity() {
    }

    public InstrumentEntity(String symbolName, Double highPrice, Double lowPrice, Double lastPrice, Double bidPrice, Double askPrice) {
        this.symbolName = symbolName;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.lastPrice = lastPrice;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Double askPrice) {
        this.askPrice = askPrice;
    }
}
