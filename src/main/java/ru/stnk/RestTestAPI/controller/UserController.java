package ru.stnk.RestTestAPI.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.WebRequest;
import ru.stnk.RestTestAPI.model.Roles;
import ru.stnk.RestTestAPI.model.User;
import ru.stnk.RestTestAPI.model.UserDTO;
import ru.stnk.RestTestAPI.repository.RolesRepository;
import ru.stnk.RestTestAPI.repository.UserRepository;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {

    /*@Autowired
    private ObjectMapper objectMapper;*/

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    private Map<String, Object> payload (Object params, Integer error, String errorDesc) {

        /*//Преобразуем HashMap параметры запроса в JSON представление
        ObjectNode objectNodeData = objectMapper.valueToTree(params);
        //objectNodeData.put("message", String.format("I'm be back, %s!", message));

        ObjectNode objectNodeMain = objectMapper.createObjectNode();
        objectNodeMain.set("data", objectNodeData);
        objectNodeMain.put("error", error);
        objectNodeMain.put("errorDesc", errorDesc);*/

        HashMap<String, Object> message = new HashMap<>();
        message.put("error", error);
        message.put("errorDesc", errorDesc);
        message.put("data", params);

        return message;

    }

    /*@GetMapping("/reg-start")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createGetUsers (@RequestParam String email,
                                               @RequestParam String password,
                                               @RequestParam String phone,
                                               @RequestParam(required = false, defaultValue = "web") String os,
                                               @RequestParam(required = false, defaultValue = "ROLE_USER") String role) {
        User user = new User();
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

        Roles roleName = rolesRepository.findByName(role);
        user.addRole(roleName);

        userRepository.save(user);

        return payload(user, 0, "");
    }*/

    /*@GetMapping("/reg-start")
    public Map<String, Object> createGetUsers (WebRequest request) {
        Map<String, String[]> requestParams =  request.getParameterMap();
        requestParams;
    }*/

    @PostMapping("/reg-start")
    @ResponseStatus(HttpStatus.CREATED)
    //(@Valid @RequestBody final UserDTO requestBody, BindingResult bindingResult)
    public Map<String, Object> createPostUser (@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {

        final String checkCode = "9999";
        final Roles roleNameDefault = rolesRepository.findByName("ROLE_USER");

        HashMap<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            FieldError errors = bindingResult.getFieldError();
            response.put("requestDataUser", userDTO);
            response.put("errors", "Поле " + "'" + errors.getField() + "'" + " " + errors.getDefaultMessage());
            return response;
        }

        response.put("checkCode", checkCode);
        response.put("requestDataUser", userDTO);

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

        return payload(response, 0, "");
    }

    @GetMapping("/hello")
    public Map<String, Object> getHello (@RequestParam HashMap<String, String> params, WebRequest request) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("data", params);
        response.put("time", new Date());
        //response.put("user", userRepository.findByEmail(params.get("email")));
        //response.put("user", userRepository.findByEmail(params.get("email")));

        //Principal principal = request.getUserPrincipal();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        response.put("user", auth.getDetails());

        return payload(response, 0, "");
    }

    /*@PostMapping("/logout")
    public String destroySession(HttpServletRequest request) {

        request.getSession().invalidate();
        return "redirect:/hello";
    }*/

    private BindingResult checkFieldRegistrationUser (@Valid final UserDTO user, BindingResult bindingResult) {
        return bindingResult;
    }
}