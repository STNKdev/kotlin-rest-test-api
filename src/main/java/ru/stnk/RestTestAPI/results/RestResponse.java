package ru.stnk.RestTestAPI.results;

public class RestResponse<T> {

    private T data;
    private int error;
    private String description;

    public RestResponse() {
        this.data = null;
        this.error = 0;
        this.description = "";
    }

    public RestResponse(int error, String description) {
        this.data = null;
        this.error = error;
        this.description = description;
    }

    public RestResponse(T data, int error, String description) {
        this.data = data;
        this.error = error;
        this.description = description;
    }

    public RestResponse(int error) {
        this.data = null;
        this.error = error;
        this.description = "";
    }

    public RestResponse(int error, Throwable ex) {
        this.data = null;
        this.error = error;
        this.description = ex.getMessage();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
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
