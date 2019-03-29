package ru.stnk.RestTestAPI.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.WebRequest;
import ru.stnk.RestTestAPI.model.RestResponse;
import ru.stnk.RestTestAPI.model.Roles;
import ru.stnk.RestTestAPI.model.User;
import ru.stnk.RestTestAPI.model.UserDTO;
import ru.stnk.RestTestAPI.repository.RolesRepository;
import ru.stnk.RestTestAPI.repository.UserRepository;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;

@Validated
@RestController
public class UserController {

    /*@Autowired
    private ObjectMapper objectMapper;*/

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    //Поля по умолчанию: Object data = null, int error = 0, String description = ""
    private RestResponse response = new RestResponse();

    @GetMapping("/reg-start")
    public RestResponse createGetUsers (@RequestParam @Email @NotBlank String email,
                                               @RequestParam @NotBlank String password,
                                               @RequestParam @NotBlank
                                                    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$") String phone,
                                               @RequestParam(required = false, defaultValue = "web") String os,
                                               @RequestParam(required = false, defaultValue = "ROLE_USER") String role) {
        final String checkCode = "9999";
        final Roles roleNameDefault = rolesRepository.findByName(role);

        HashMap<String, Object> data = new HashMap<>();

        data.put("checkCode", checkCode);

        response.setData(data);


        /*User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setOs(os);
        user.setEnableUser(true);
        user.setEmailConfirmed(false);
        user.setBetBalance((long) 0);
        user.setFreeBalance((long) 0);
        user.setWithdrawalBalance((long) 0);
        //user.setRoles(new ArrayList<>());
        user.addRole(roleName);

        userRepository.save(user);*/

        return response;
    }

    /*@GetMapping("/reg-start")
    public Map<String, Object> createGetUsers (WebRequest request) {
        Map<String, String[]> requestParams =  request.getParameterMap();
        requestParams;
    }*/

    @PostMapping("/reg-start")
    @ResponseStatus(HttpStatus.CREATED)
    //(@Valid @RequestBody final UserDTO requestBody, BindingResult bindingResult)
    public RestResponse createPostUser (@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {

        final String checkCode = "9999";
        final Roles roleNameDefault = rolesRepository.findByName("ROLE_USER");

        HashMap<String, Object> data = new HashMap<>();

        if (bindingResult.hasErrors()) {
            FieldError errors = bindingResult.getFieldError();
            /*response.put("requestDataUser", userDTO);
            response.put("errors", "Поле " + "'" + errors.getField() + "'" + " " + errors.getDefaultMessage());*/

            response.setError(HttpStatus.EXPECTATION_FAILED.value());
            response.setDescription("Поле " + "'" + errors.getField() + "'" + " " + errors.getDefaultMessage());

            return response;
        }

        data.put("checkCode", checkCode);

        response.setData(data);

        /*User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setOs(userDTO.getOs());
        user.setEnableUser(false);
        user.setEmailConfirmed(false);
        user.setBetBalance((long) 0);
        user.setFreeBalance((long) 0);
        user.setWithdrawalBalance((long) 0);
        //user.setRoles(new ArrayList<>());
        user.addRole(roleNameDefault);

        userRepository.save(user);*/

        return response;
    }

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

    /*@PostMapping("/logout")
    public String destroySession(HttpServletRequest request) {

        request.getSession().invalidate();
        return "redirect:/hello";
    }*/
}