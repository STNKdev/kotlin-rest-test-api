package ru.stnk.RestTestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ru.stnk.RestTestAPI.configuration.SecurityConfig;
import ru.stnk.RestTestAPI.entity.User;
import ru.stnk.RestTestAPI.results.RestResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
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
    private FindByIndexNameSessionRepository sessionRepository;*/

    @Autowired
    private SecurityConfig securityConfig;

    /*@Autowired
    private ProviderManager authManager;*/

    @GetMapping("/info")
    public RestResponse getInfo (/*@AuthenticationPrincipal User user, */WebRequest request) {

        RestResponse restResponse = new RestResponse();

        Principal principal = request.getUserPrincipal();

        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //data.put("user", RequestContextHolder.currentRequestAttributes().getSessionId());

        Map<String, Object> data = new HashMap<>();

        data.put("user", principal);

        restResponse.setData(data);

        return restResponse;
    }

    @GetMapping("/auth")
    public RestResponse getAuth (/*@RequestParam String email, @RequestParam String pass*/HttpServletRequest request) throws Exception {
        RestResponse restResponse = new RestResponse();

        UsernamePasswordAuthenticationToken tokenAuth = new UsernamePasswordAuthenticationToken("admin@test.io", "123");
        //без этого не обновляется SecurityContextHolder (Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated)
        tokenAuth.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = securityConfig.authenticationManagerBean().authenticate(tokenAuth);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        restResponse.setData(tokenAuth.getDetails());

        return restResponse;
    }

}