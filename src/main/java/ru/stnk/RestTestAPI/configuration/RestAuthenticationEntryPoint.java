package ru.stnk.RestTestAPI.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.stnk.RestTestAPI.results.RestResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");

        RestResponse restResponse = new RestResponse(401,
                "Необходима аутентификация.");

        try {
            response.getWriter().print(new ObjectMapper().writeValueAsString(restResponse));
        } catch (JsonProcessingException ex) {
            response.getWriter().print(ex.toString());
        }

        response.getWriter().flush();
    }
}
