package ru.stnk.RestTestAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.stnk.RestTestAPI.model.User;
import ru.stnk.RestTestAPI.repository.UserRepository;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;

@RestController
public class UserController {

    /*@Autowired
    private ObjectMapper objectMapper;*/

    @Autowired
    private UserRepository userRepository;

    private String checkCode = "9999";

    private HashMap<String, Object> payload (HashMap<String, Object> params, Integer error, String errorDesc) {

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

    @GetMapping("/reg-start")
    //@ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> getUsers (@RequestParam HashMap<String, String> params) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("users", userRepository.findAll());
        return payload(response, 0, "");
    }

    @PostMapping("/reg-start")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createUser (@Valid @RequestBody User requestBody) {

        userRepository.save(requestBody);

        HashMap<String, Object> response = new HashMap<>();
        response.put("checkCode", checkCode);

        return payload(response, 0, "");
    }

    @GetMapping("/hello")
    public HashMap<String, Object> getHello (@RequestParam HashMap<String, String> params) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("data", params);
        response.put("time", new Date());
        response.put("user", userRepository.findByEmail(params.get("email")));
        return payload(response, 0, "");
    }
}