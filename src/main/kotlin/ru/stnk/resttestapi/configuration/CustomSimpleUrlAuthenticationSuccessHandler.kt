package ru.stnk.resttestapi.configuration

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import ru.stnk.resttestapi.results.RestResponse
import java.io.IOException
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomSimpleUrlAuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {

    //private RequestCache requestCache = new HttpSessionRequestCache();

    @Throws(ServletException::class, IOException::class)
    override fun onAuthenticationSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication) {

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

        response.status = HttpStatus.OK.value()

        response.contentType = "application/json; charset=UTF-8"

        val restResponse = RestResponse()

        val data = HashMap<String, String>()

        data["session_id"] = request.session.id

        //restResponse.setData(data)
        restResponse.data = data

        try {
            response.writer.print(ObjectMapper().writeValueAsString(restResponse))
        } catch (ex: JsonProcessingException) {
            response.writer.print(ex.toString())
        }

        response.writer.flush()
        clearAuthenticationAttributes(request)
    }

    /*public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }*/
}
