package ru.stnk.RestTestAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationsResponse {
    // data с котировками
    private QuotationsData[] data;
    // URL для WebSocket соединений
    private String[] zmq;

    public QuotationsResponse() {
    }

    public QuotationsData[] getData() {
        return data;
    }

    public void setData(QuotationsData[] data) {
        this.data = data;
    }

    public String[] getZmq() {
        return zmq;
    }

    public void setZmq(String[] zmq) {
        this.zmq = zmq;
    }

    @Override
    public String toString() {
        return "QuotationsResponse{" +
                "data=" + Arrays.toString(data) +
                ", zmq=" + Arrays.toString(zmq) +
                '}';
    }
}
