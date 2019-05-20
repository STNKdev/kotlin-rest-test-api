package ru.stnk.RestTestAPI.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.stnk.RestTestAPI.exception.registration.*;
import ru.stnk.RestTestAPI.results.RestResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions (Exception ex,
                                                       /*HttpStatus status,*/
                                                       WebRequest request) {

        RestResponse restResponse = new RestResponse (HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(restResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException (BadCredentialsException ex,
                                                                 WebRequest request) {
        RestResponse restResponse = new RestResponse (105, ex.getLocalizedMessage());
        return new ResponseEntity<>(restResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException (AccessDeniedException ex,
                                                              WebRequest request) {
        RestResponse restResponse = new RestResponse (HttpStatus.FORBIDDEN.value(), "Доступ запрещен, необходима аутентификация");
        return new ResponseEntity<>(restResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Object> handleIncorrectLoginException (IncorrectPasswordException ex,
                                                                 WebRequest request) {
        RestResponse restResponse = new RestResponse (104, "Не указано/пустое поле пароля или длина меньше 3");
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<Object> handleUserExistException (UserExistException ex,
                                                            WebRequest request) {
        RestResponse restResponse = new RestResponse (106, "Пользователь существует");
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @ExceptionHandler(IncorrectEmailException.class)
    public ResponseEntity<Object> handleIncorrectEmailException (IncorrectEmailException ex,
                                                                 WebRequest request) {
        RestResponse restResponse = new RestResponse (107, "Некорректный email");
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @ExceptionHandler(LoginPasswordEqualException.class)
    public ResponseEntity<Object> handleIncorrectEmailException (LoginPasswordEqualException ex,
                                                                 WebRequest request) {
        RestResponse restResponse = new RestResponse (108, "Пароль не должен совпадать с логином");
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @ExceptionHandler(IncorrectPhoneException.class)
    public ResponseEntity<Object> handleIncorrectPhoneException (IncorrectPhoneException ex,
                                                                 WebRequest request) {
        RestResponse restResponse = new RestResponse (111, "Некорректный номер телефона");
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @ExceptionHandler(DelayException.class)
    public ResponseEntity<Object> handleDelayAttempsException (DelayException ex,
                                                               WebRequest request) {
        RestResponse restResponse = new RestResponse (112, "Слишком рано был запрошен повторный вызов кода подтверждения");
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable (HttpMessageNotReadableException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        RestResponse restResponse = new RestResponse(status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(restResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        RestResponse restResponse = new RestResponse(status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(restResponse, status);
    }
}