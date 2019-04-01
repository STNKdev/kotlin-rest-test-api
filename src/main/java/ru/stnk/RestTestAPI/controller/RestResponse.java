package ru.stnk.RestTestAPI.controller;

public class RestResponse {

    private Object data = null;
    private int error = 0;
    private String description = "";

    public RestResponse() {}

    public RestResponse(int error, String description) {
        this.error = error;
        this.description = description;
    }

    public RestResponse(Object data, int error, String description) {
        this.data = data;
        this.error = error;
        this.description = description;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
