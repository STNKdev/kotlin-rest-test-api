package ru.stnk.RestTestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.stnk.RestTestAPI.results.RestResponse;
import ru.stnk.RestTestAPI.service.ControllerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/*@Validated*/
@RestController
public class UserController {

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

    @Autowired
    ControllerService controllerService;

    @GetMapping("/userinfo")
    public RestResponse getUserInfo (/*@AuthenticationPrincipal UserDetails userDetails, */HttpServletRequest request) {

        RestResponse restResponse = new RestResponse();

        Principal principal = request.getUserPrincipal();

        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        restResponse.setData(controllerService.getUser(principal.getName()));

        return restResponse;
    }

    @GetMapping("/auth")
    public RestResponse getAuth (@RequestParam String email,
                                 @RequestParam String pass,
                                 HttpServletRequest request)
            throws ServletException, BadCredentialsException {

        RestResponse restResponse = new RestResponse();

        //Session session = controllerService.registerUserSecurityContext(email, pass, request);

        //httpServletResponse.setHeader("SET-COOKIE", "SESSION=" + session.getId()); //controllerService.registerUserSecurityContext(email, pass, request).toString();

        try {
            request.login(email, pass);
        } catch (ServletException ex) {
            restResponse.setError(105);
            restResponse.setDescription("Неправильная пара логин-пароль. Либо нет пользователя с таким логином, либо ошибка в пароле.");
        }

        HttpSession session = request.getSession();

        Map<String, String> data = new HashMap<>();

        data.put("session_id", session.getId());

        restResponse.setData(data);

        //restResponse.setData(controllerService.registerUserSecurityContext(email, pass, request));

        return restResponse;
    }

}