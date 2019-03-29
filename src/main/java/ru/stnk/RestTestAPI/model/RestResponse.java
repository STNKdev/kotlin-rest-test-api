package ru.stnk.RestTestAPI.model;

public class RestResponse {

    private Object data = null;
    private int error = 0;
    private String description = "";

    public RestResponse() {}

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
