package ru.stnk.RestTestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.stnk.RestTestAPI.dto.UserDTO;
import ru.stnk.RestTestAPI.exception.registration.*;
import ru.stnk.RestTestAPI.results.RestResponse;
import ru.stnk.RestTestAPI.service.ControllerService;

import javax.servlet.http.HttpServletRequest;
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
    private ControllerService controllerService;

    /*
     *
     * Обработчики GET и POST запросов /reg-start
     *
     * используются для генерации проверочного кода для следующего метода
     * */

    @GetMapping("/reg-start")
    public RestResponse preRegistrationGetMethod (
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam String os,
            @RequestParam(required = false, defaultValue = "true") String viaEmail
    ) throws IncorrectEmailException,
            LoginPasswordEqualException,
            IncorrectPasswordException,
            IncorrectPhoneException,
            DelayException {

        RestResponse response = new RestResponse();

        //HashMap<String, Object> data = new HashMap<>();

        // Пароль не должен быть email-ом
        if (password.equals(email)) {
            throw new LoginPasswordEqualException();
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setPhone(phone);
        userDTO.setOs(os);
        userDTO.setViaEmail(viaEmail.equals("true"));

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
        * violation.getInvalidValue() - возвращает значение из-за которого возникла ошибка
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

        //data.put("checkCode", controllerService.saveCheckCodeToEmail(userDTO.getEmail()));

        //controllerService.sendCheckCodeToEmail(userDTO.getEmail());

        response.setData(controllerService.saveCheckCodeToEmail(userDTO.getEmail(), userDTO.isViaEmail()));

        return response;
    }

    @PostMapping("/reg-start")
    //(@Valid @RequestBody final UserDTO requestBody, BindingResult bindingResult)
    public RestResponse preRegistrationPostMethod (
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

        //data.put("checkCode", controllerService.saveCheckCodeToEmail(userDTO.getEmail()));

        //controllerService.sendCheckCodeToEmail(userDTO.getEmail());

        response.setData(controllerService.saveCheckCodeToEmail(userDTO.getEmail(), userDTO.isViaEmail()));

        return response;
    }

    /*
    *
    * Обработчики GET и POST запросов /reg-confirm
    *
    * используется для завершения регистрации с проверочным кодом из предыдущего метода
    * */


    @GetMapping("/reg-confirm")
    public RestResponse registrationConfirmGetMethod (
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam String os,
            @RequestParam String code,
            @RequestParam(required = false, defaultValue = "true") String viaEmail,
            HttpServletRequest request
    ) throws IncorrectEmailException,
            LoginPasswordEqualException,
            IncorrectPasswordException,
            IncorrectPhoneException,
            DelayException,
            UserExistException {

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
        userDTO.setViaEmail(viaEmail.equals("true"));

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
         * violation.getInvalidValue() - возвращает значение из-за которого возникла ошибка
         * */
        if (!violations.isEmpty()) {

            HashMap<String, String> errors = new HashMap<>();
            for (ConstraintViolation<UserDTO> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            if (errors.containsKey("email")) {
                throw new IncorrectEmailException();
            } else if (errors.containsKey("password")) {
                throw new IncorrectPasswordException();
            } else if (errors.containsKey("phone")) {
                throw new IncorrectPhoneException();
            }
        }

        //data.put("checkCode", controllerService.saveCheckCodeToEmail(userDTO.getEmail()));

        //controllerService.sendCheckCodeToEmail(userDTO.getEmail());

        response.setData(controllerService.checkOfVerificationCode(userDTO, code, request));

        return response;
    }

    @PostMapping("/reg-confirm")
    //(@Valid @RequestBody final UserDTO requestBody, BindingResult bindingResult)
    public RestResponse registrationConfirmPostMethod (
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult,
            HttpServletRequest request
    ) throws IncorrectEmailException,
            LoginPasswordEqualException,
            IncorrectPasswordException,
            IncorrectPhoneException,
            DelayException,
            UserExistException {

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

        //data.put("checkCode", controllerService.saveCheckCodeToEmail(userDTO.getEmail()));

        //controllerService.sendCheckCodeToEmail(userDTO.getEmail());

        response.setData(controllerService.checkOfVerificationCode(userDTO, userDTO.getCode(), request));

        return response;
    }

}
