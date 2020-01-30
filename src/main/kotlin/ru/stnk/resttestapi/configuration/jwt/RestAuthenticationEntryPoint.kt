package ru.stnk.resttestapi.configuration.jwt

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import ru.stnk.resttestapi.results.RestResponse
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

    private val logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint::class.java)

    @Throws(IOException::class)
    override fun commence(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authException: AuthenticationException) {

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json; charset=UTF-8"

        val restResponse = RestResponse(401,
                "Необходима авторизация.")

        try {
            logger.error("Unauthorized error. Message - {}", authException.message)
            response.writer.print(ObjectMapper().writeValueAsString(restResponse))
        } catch (ex: JsonProcessingException) {
            logger.error("JsonProcessingException. Message - {}", ex.toString())
            //response.writer.print(ex.toString())
        }

        response.writer.flush()
    }
}
