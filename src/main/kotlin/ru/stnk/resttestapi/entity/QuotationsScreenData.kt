package ru.stnk.resttestapi.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class QuotationsScreenData {
    var pairs_data: Array<QuotationsPairsData>? = null

    override fun toString(): String {
        return "QuotationsScreenData{" +
                "pairs_data=" + Arrays.toString(pairs_data) +
                '}'.toString()
    }
}
