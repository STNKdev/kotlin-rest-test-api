package ru.stnk.resttestapi.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class QuotationsResponse {

    // data с котировками
    var data: Array<QuotationsData>? = null

    // URL для WebSocket соединений
    var zmq: Array<String>? = null

    override fun toString(): String {
        return "QuotationsResponse{" +
                "data=" + Arrays.toString(data) +
                ", zmq=" + Arrays.toString(zmq) +
                '}'.toString()
    }
}
