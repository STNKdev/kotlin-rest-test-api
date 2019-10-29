package ru.stnk.RestTestAPI.entity.coins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.stnk.RestTestAPI.entity.AuditModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class QuoteBinOneMinute extends AuditModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column
    private String symbolName;

    @Column
    private Double bidPrice;

    @Column
    private Double askPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date timestamp;

    public QuoteBinOneMinute() {}

    public QuoteBinOneMinute(String symbolName, Double bidPrice, Double askPrice, Date timestamp) {
        this.symbolName = symbolName;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.timestamp = timestamp;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
