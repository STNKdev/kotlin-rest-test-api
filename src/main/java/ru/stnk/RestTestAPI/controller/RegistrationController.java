package ru.stnk.RestTestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.stnk.RestTestAPI.dto.UserDTO;
import ru.stnk.RestTestAPI.exception.registration.*;
import ru.stnk.RestTestAPI.results.RestResponse;
import ru.stnk.RestTestAPI.service.UserService;

import javax.validation.*;
import java.util.HashMap;
import java.util.Set;

@RestController
public class RegistrationController {

    /*@Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private MailSender mailSender;*/

    @Autowired
    private UserService userService;

    @GetMapping("/reg-start")
    public RestResponse getCreateUsers (
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam String os
    ) throws IncorrectEmailException,
            LoginPasswordEqualException,
            IncorrectPasswordException,
            IncorrectPhoneException,
            DelayException {

        RestResponse response = new RestResponse();

        //HashMap<String, Object> data = new HashMap<>();

        if (password.equals(email)) {
            throw new LoginPasswordEqualException();
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setPhone(phone);
        userDTO.setOs(os);

        /*
        * Про Validator взял отсюда
        * https://www.javaquery.com/2018/02/constraints-validation-for-user-inputs.html
        *
        * https://habr.com/ru/post/68318/
        */
        ValidatorFactory validationFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validationFactory.getValidator();
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        /*
        * violation.getPropertyPath().toString() - возвращает имя поля в котором возникла ошибка
        * violation.getMessage() - возвращает сообщение об ошибке
        * violation.getInvalidValue() - возвращает значиние из-за которого возникла ошибка
        * */
        if (!violations.isEmpty()) {

            HashMap<String, String> errors = new HashMap<>();
            for (ConstraintViolation<UserDTO> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            if (errors.containsKey("email")) {
                /*response.setError(107);
                response.setDescription(data.get("email").toString());
                return response;*/
                throw new IncorrectEmailException();
            } else if (errors.containsKey("password")) {
                throw new IncorrectPasswordException();
            } else if (errors.containsKey("phone")) {
                throw new IncorrectPhoneException();
            }
        }

        //data.put("checkCode", userService.saveCheckCodeToEmail(userDTO.getEmail()));

        //userService.sendCheckCodeToEmail(userDTO.getEmail());

        response.setData(userService.saveCheckCodeToEmail(userDTO.getEmail()));

        return response;
    }

    @PostMapping("/reg-start")
    //(@Valid @RequestBody final UserDTO requestBody, BindingResult bindingResult)
    public RestResponse postCreateUser (
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) throws IncorrectEmailException,
            LoginPasswordEqualException,
            IncorrectPasswordException,
            IncorrectPhoneException,
            DelayException {

        RestResponse response = new RestResponse();

        //HashMap<String, Object> data = new HashMap<>();

        if (userDTO.getPassword().equals(userDTO.getEmail())) {
            throw new LoginPasswordEqualException();
        }

        if (bindingResult.hasErrors()) {
            FieldError errors = bindingResult.getFieldError();

            if (errors.getField().equals("email")) {
                throw new IncorrectEmailException();
            } else if (errors.getField().equals("password")) {
                throw new IncorrectPasswordException();
            } else if (errors.getField().equals("phone")) {
                throw new IncorrectPhoneException();
            }
        }

        //data.put("checkCode", userService.saveCheckCodeToEmail(userDTO.getEmail()));

        //userService.sendCheckCodeToEmail(userDTO.getEmail());

        response.setData(userService.saveCheckCodeToEmail(userDTO.getEmail()));

        return response;
    }
}
