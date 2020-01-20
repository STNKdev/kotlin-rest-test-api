package ru.stnk.resttestapi.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class QuotationsData {
    var screen_data: QuotationsScreenData? = null

    override fun toString(): String {
        return "QuotationsData{" +
                "screen_data=" + screen_data +
                '}'.toString()
    }
}
