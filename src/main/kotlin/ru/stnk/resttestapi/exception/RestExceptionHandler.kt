package ru.stnk.resttestapi.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.lang.Nullable
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.stnk.resttestapi.exception.registration.*
import ru.stnk.resttestapi.results.RestResponse
import java.util.*

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception,
            /*HttpStatus status,*/
                            request: WebRequest): ResponseEntity<Any> {

        val restResponse = RestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.localizedMessage)
        return ResponseEntity(restResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException,
                                      request: WebRequest): ResponseEntity<Any> {
        val restResponse = RestResponse(105, ex.localizedMessage)
        return ResponseEntity(restResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException,
                                    request: WebRequest): ResponseEntity<Any> {
        val restResponse = RestResponse(HttpStatus.FORBIDDEN.value(), "Доступ запрещен, необходима аутентификация")
        return ResponseEntity(restResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(IncorrectPasswordException::class)
    fun handleIncorrectLoginException(ex: IncorrectPasswordException,
                                      request: WebRequest): ResponseEntity<Any> {
        val restResponse = RestResponse(104, "Не указано/пустое поле пароля или длина меньше 3")
        return ResponseEntity(restResponse, HttpStatus.OK)
    }

    @ExceptionHandler(UserExistException::class)
    fun handleUserExistException(ex: UserExistException,
                                 request: WebRequest): ResponseEntity<Any> {
        val restResponse = RestResponse(106, "Пользователь существует")
        return ResponseEntity(restResponse, HttpStatus.OK)
    }

    @ExceptionHandler(IncorrectEmailException::class)
    fun handleIncorrectEmailException(ex: IncorrectEmailException,
                                      request: WebRequest): ResponseEntity<Any> {
        val restResponse = RestResponse(107, "Некорректный email")
        return ResponseEntity(restResponse, HttpStatus.OK)
    }

    @ExceptionHandler(LoginPasswordEqualException::class)
    fun handleIncorrectEmailException(ex: LoginPasswordEqualException,
                                      request: WebRequest): ResponseEntity<Any> {
        val restResponse = RestResponse(108, "Пароль не должен совпадать с логином")
        return ResponseEntity(restResponse, HttpStatus.OK)
    }

    @ExceptionHandler(IncorrectPhoneException::class)
    fun handleIncorrectPhoneException(ex: IncorrectPhoneException,
                                      request: WebRequest): ResponseEntity<Any> {
        val restResponse = RestResponse(111, "Некорректный номер телефона")
        return ResponseEntity(restResponse, HttpStatus.OK)
    }

    @ExceptionHandler(DelayException::class)
    fun handleDelayAttempsException(ex: DelayException,
                                    request: WebRequest): ResponseEntity<Any> {
        val restResponse = RestResponse(112, "Слишком рано был запрошен повторный вызов кода подтверждения")
        if (ex.delay != null) {
            val delayInfo: MutableMap<String, Long?> = HashMap()
            delayInfo["secondsUntilResend"] = ex.delay
            restResponse.data = delayInfo
        }
        return ResponseEntity(restResponse, HttpStatus.OK)
    }

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException,
                                              headers: HttpHeaders,
                                              status: HttpStatus,
                                              request: WebRequest): ResponseEntity<Any> {
        val restResponse = RestResponse(status.value(), ex.localizedMessage)
        return ResponseEntity(restResponse, status)
    }

    override fun handleExceptionInternal(ex: java.lang.Exception,
                                         @Nullable body: Any?,
                                         headers: HttpHeaders,
                                         status: HttpStatus,
                                         request: WebRequest): ResponseEntity<Any> {
        val restResponse = RestResponse(status.value(), ex.localizedMessage)
        return ResponseEntity(restResponse, status)
    }
}