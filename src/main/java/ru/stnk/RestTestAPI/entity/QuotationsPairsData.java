package ru.stnk.RestTestAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationsPairsData {
    private int pair_ID;
    private String last;
    private String change_percent_val;
    private String change_precent;
    private String change_val;
    private String change;
    private String localized_last_step_arrow;
    private boolean exchange_is_open;
    private String last_timestamp;
    private boolean is_cfd;
    private String earning_alert;
    private String exchange_name;
    private String pair_name;
    private String currency_in;
    private String exchange_symbol;
    private String exchange_country_ID;
    private String zmqIsOpen;

    public QuotationsPairsData() {
    }

    public int getPair_ID() {
        return pair_ID;
    }

    public void setPair_ID(int pair_ID) {
        this.pair_ID = pair_ID;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getChange_percent_val() {
        return change_percent_val;
    }

    public void setChange_percent_val(String change_percent_val) {
        this.change_percent_val = change_percent_val;
    }

    public String getChange_precent() {
        return change_precent;
    }

    public void setChange_precent(String change_precent) {
        this.change_precent = change_precent;
    }

    public String getChange_val() {
        return change_val;
    }

    public void setChange_val(String change_val) {
        this.change_val = change_val;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getLocalized_last_step_arrow() {
        return localized_last_step_arrow;
    }

    public void setLocalized_last_step_arrow(String localized_last_step_arrow) {
        this.localized_last_step_arrow = localized_last_step_arrow;
    }

    public boolean isExchange_is_open() {
        return exchange_is_open;
    }

    public void setExchange_is_open(boolean exchange_is_open) {
        this.exchange_is_open = exchange_is_open;
    }

    public String getLast_timestamp() {
        return last_timestamp;
    }

    public void setLast_timestamp(String last_timestamp) {
        this.last_timestamp = last_timestamp;
    }

    public boolean isIs_cfd() {
        return is_cfd;
    }

    public void setIs_cfd(boolean is_cfd) {
        this.is_cfd = is_cfd;
    }

    public String getEarning_alert() {
        return earning_alert;
    }

    public void setEarning_alert(String earning_alert) {
        this.earning_alert = earning_alert;
    }

    public String getExchange_name() {
        return exchange_name;
    }

    public void setExchange_name(String exchange_name) {
        this.exchange_name = exchange_name;
    }

    public String getPair_name() {
        return pair_name;
    }

    public void setPair_name(String pair_name) {
        this.pair_name = pair_name;
    }

    public String getCurrency_in() {
        return currency_in;
    }

    public void setCurrency_in(String currency_in) {
        this.currency_in = currency_in;
    }

    public String getExchange_symbol() {
        return exchange_symbol;
    }

    public void setExchange_symbol(String exchange_symbol) {
        this.exchange_symbol = exchange_symbol;
    }

    public String getExchange_country_ID() {
        return exchange_country_ID;
    }

    public void setExchange_country_ID(String exchange_country_ID) {
        this.exchange_country_ID = exchange_country_ID;
    }

    public String getZmqIsOpen() {
        return zmqIsOpen;
    }

    public void setZmqIsOpen(String zmqIsOpen) {
        this.zmqIsOpen = zmqIsOpen;
    }

    @Override
    public String toString() {
        return "QuotationsPairsData{" +
                "pair_ID=" + pair_ID +
                ", last='" + last + '\'' +
                ", change_percent_val='" + change_percent_val + '\'' +
                ", change_precent='" + change_precent + '\'' +
                ", change_val='" + change_val + '\'' +
                ", change='" + change + '\'' +
                ", localized_last_step_arrow='" + localized_last_step_arrow + '\'' +
                ", exchange_is_open=" + exchange_is_open +
                ", last_timestamp='" + last_timestamp + '\'' +
                ", is_cfd=" + is_cfd +
                ", earning_alert='" + earning_alert + '\'' +
                ", exchange_name='" + exchange_name + '\'' +
                ", pair_name='" + pair_name + '\'' +
                ", currency_in='" + currency_in + '\'' +
                ", exchange_symbol='" + exchange_symbol + '\'' +
                ", exchange_country_ID='" + exchange_country_ID + '\'' +
                ", zmqIsOpen='" + zmqIsOpen + '\'' +
                '}';
    }
}
