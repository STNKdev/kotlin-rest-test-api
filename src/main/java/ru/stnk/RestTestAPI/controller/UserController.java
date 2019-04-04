package ru.stnk.RestTestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ru.stnk.RestTestAPI.repository.UserRepository;
import ru.stnk.RestTestAPI.results.RestResponse;

import java.util.Date;
import java.util.HashMap;

@Validated
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //Поля по умолчанию: Object data = null, int error = 0, String description = ""
    //data может быть HashMap
    private RestResponse response = new RestResponse();

    @GetMapping("/hello")
    public RestResponse getHello (@RequestParam HashMap<String, String> params, WebRequest request) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("data", params);
        data.put("time", new Date());
        //response.put("user", userRepository.findByEmail(params.get("email")));
        //response.put("user", userRepository.findByEmail(params.get("email")));

        //Principal principal = request.getUserPrincipal();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        data.put("user", auth.getDetails());

        response.setData(data);

        return response;
    }

}