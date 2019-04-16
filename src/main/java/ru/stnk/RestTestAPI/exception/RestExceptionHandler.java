package ru.stnk.RestTestAPI.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.stnk.RestTestAPI.results.RestResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions (Exception ex,
                                             HttpHeaders headers,
                                             HttpStatus status,
                                             WebRequest request) {
        RestResponse restResponse = new RestResponse (status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(restResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable (HttpMessageNotReadableException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        RestResponse restResponse = new RestResponse(status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(restResponse, status);
    }
}