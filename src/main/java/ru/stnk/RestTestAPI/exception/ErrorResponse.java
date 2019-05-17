package ru.stnk.RestTestAPI.exception;

import java.util.ArrayList;
import java.util.List;


/*@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)*/
public class ErrorResponse<T> {
    private List<T> errors = new ArrayList<>(1);

    public ErrorResponse(List<T> errors) {
        this.errors = errors;
    }

    public List<T> getErrors() {
        return errors;
    }
}