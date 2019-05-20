package ru.stnk.RestTestAPI.configuration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ru.stnk.RestTestAPI.results.RestResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("text/plain; charset=UTF-8");
        /*Map<String, String> data = new HashMap<>();

        data.put("error", "105");
        data.put("description" ,"Неправильная пара логин-пароль. Либо нет пользователя с таким логином, либо ошибка в пароле.");*/

        RestResponse restResponse = new RestResponse(105,
                "Неправильная пара логин-пароль. Либо нет пользователя с таким логином, либо ошибка в пароле.");

        try {
            response.getWriter().print(new ObjectMapper().writeValueAsString(restResponse));
        } catch (JsonProcessingException ex) {
            response.getWriter().print(ex.toString());
        }

        response.getWriter().flush();
    }
}
