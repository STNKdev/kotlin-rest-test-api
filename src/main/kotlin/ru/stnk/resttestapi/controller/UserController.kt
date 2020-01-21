package ru.stnk.resttestapi.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.stnk.resttestapi.results.RestResponse
import ru.stnk.resttestapi.service.ControllerService
import java.security.Principal
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest

/*@Validated*/
@RestController
class UserController (
        @Autowired val controllerService: ControllerService
) {

    /*@Autowired
    private UserRepository userRepository;*/

    //Поля по умолчанию: Object data = null, int error = 0, String description = ""
    //data может быть HashMap
    //private RestResponse response = new RestResponse();

    /*@Autowired
    private FindByIndexNameSessionRepository sessionRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;*/

    @GetMapping("/userinfo")
    fun getUserInfo(/*@AuthenticationPrincipal UserDetails userDetails, */request: HttpServletRequest): RestResponse {

        val restResponse = RestResponse()

        val principal: Principal = request.userPrincipal

        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        restResponse.data = controllerService.getUser(principal.name)

        return restResponse
    }

    @GetMapping("/auth")
    @Throws(ServletException::class, BadCredentialsException::class)
    fun getAuth(@RequestParam email: String,
                @RequestParam pass: String,
                request: HttpServletRequest): RestResponse {

        val restResponse = RestResponse()

        //Session session = controllerService.registerUserSecurityContext(email, pass, request);

        //httpServletResponse.setHeader("SET-COOKIE", "SESSION=" + session.getId()); //controllerService.registerUserSecurityContext(email, pass, request).toString();

        try {
            request.login(email, pass)
        } catch (ex: ServletException) {
            restResponse.error = 105
            restResponse.description = "Неправильная пара логин-пароль. Либо нет пользователя с таким логином, либо ошибка в пароле."
        }

        val session = request.session

        val data: MutableMap<String, String> = HashMap()

        data["session_id"] = session.id

        restResponse.data = data

        //restResponse.setData(controllerService.registerUserSecurityContext(email, pass, request));

        return restResponse
    }

}