package ru.stnk.resttestapi.configuration


import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import ru.stnk.resttestapi.results.RestResponse
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomAuthenticationFailureHandler : AuthenticationFailureHandler {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(request: HttpServletRequest,
                                         response: HttpServletResponse,
                                         exception: AuthenticationException) {

        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json; charset=UTF-8"
        /*Map<String, String> data = new HashMap<>();

        data.put("error", "105");
        data.put("description" ,"Неправильная пара логин-пароль. Либо нет пользователя с таким логином, либо ошибка в пароле.");*/

        val restResponse = RestResponse(105,
                "Неправильная пара логин-пароль. Либо нет пользователя с таким логином, либо ошибка в пароле.")

        try {
            response.writer.print(ObjectMapper().writeValueAsString(restResponse))
        } catch (ex: JsonProcessingException) {
            response.writer.print(ex.toString())
        }

        response.writer.flush()
    }
}
