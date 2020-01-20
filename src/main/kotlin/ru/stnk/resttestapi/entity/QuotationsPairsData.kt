package ru.stnk.resttestapi.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class QuotationsPairsData {
    var pair_ID: Int = 0
    var last: String? = null
    var change_percent_val: String? = null
    var change_precent: String? = null
    var change_val: String? = null
    var change: String? = null
    var localized_last_step_arrow: String? = null
    var isExchange_is_open: Boolean = false
    var last_timestamp: String? = null
    var isIs_cfd: Boolean = false
    var earning_alert: String? = null
    var exchange_name: String? = null
    var pair_name: String? = null
    var currency_in: String? = null
    var exchange_symbol: String? = null
    var exchange_country_ID: String? = null
    var zmqIsOpen: String? = null

    override fun toString(): String {
        return "QuotationsPairsData{" +
                "pair_ID=" + pair_ID +
                ", last='" + last + '\''.toString() +
                ", change_percent_val='" + change_percent_val + '\''.toString() +
                ", change_precent='" + change_precent + '\''.toString() +
                ", change_val='" + change_val + '\''.toString() +
                ", change='" + change + '\''.toString() +
                ", localized_last_step_arrow='" + localized_last_step_arrow + '\''.toString() +
                ", exchange_is_open=" + isExchange_is_open +
                ", last_timestamp='" + last_timestamp + '\''.toString() +
                ", is_cfd=" + isIs_cfd +
                ", earning_alert='" + earning_alert + '\''.toString() +
                ", exchange_name='" + exchange_name + '\''.toString() +
                ", pair_name='" + pair_name + '\''.toString() +
                ", currency_in='" + currency_in + '\''.toString() +
                ", exchange_symbol='" + exchange_symbol + '\''.toString() +
                ", exchange_country_ID='" + exchange_country_ID + '\''.toString() +
                ", zmqIsOpen='" + zmqIsOpen + '\''.toString() +
                '}'.toString()
    }
}
