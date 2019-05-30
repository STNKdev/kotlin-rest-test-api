package ru.stnk.RestTestAPI.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.stnk.RestTestAPI.results.RestResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomSimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    //private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws ServletException, IOException {

        /*SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            clearAuthenticationAttributes(request);
            return;
        }
        String targetUrlParam = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParam != null
                && StringUtils.hasText(request.getParameter(targetUrlParam)))) {
            requestCache.removeRequest(request, response);
            clearAuthenticationAttributes(request);
            return;
        }*/

        response.setStatus(HttpStatus.OK.value());

        response.setContentType("text/plain; charset=UTF-8");

        RestResponse restResponse = new RestResponse();

        Map<String, String> data = new HashMap<>();

        data.put("session_id", request.getSession().getId());

        restResponse.setData(data);

        try {
            response.getWriter().print(new ObjectMapper().writeValueAsString(restResponse));
        } catch (JsonProcessingException ex) {
            response.getWriter().print(ex.toString());
        }

        response.getWriter().flush();
        clearAuthenticationAttributes(request);
    }

    /*public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }*/
}
