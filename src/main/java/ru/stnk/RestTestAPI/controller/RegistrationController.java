package ru.stnk.RestTestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.stnk.RestTestAPI.dto.UserDTO;
import ru.stnk.RestTestAPI.entity.Roles;
import ru.stnk.RestTestAPI.entity.User;
import ru.stnk.RestTestAPI.repository.RolesRepository;
import ru.stnk.RestTestAPI.repository.UserRepository;
import ru.stnk.RestTestAPI.results.RestResponse;
import ru.stnk.RestTestAPI.service.MailSender;

import javax.validation.*;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@RestController
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private MailSender mailSender;

    @GetMapping("/reg-start")
    public RestResponse getCreateUsers (
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam String os
    ) {

        final Roles roleNameDefault = rolesRepository.findByName("ROLE_USER");
        RestResponse response = new RestResponse();

        HashMap<String, Object> data = new HashMap<>();

        Optional<User> usr = userRepository.findByEmail(email);

        if (usr.isPresent()) {
            response.setError(106);
            response.setDescription("Пользователь существует");
            return response;
        }

        if (password.equals(email)) {
            response.setError(108);
            response.setDescription("Пароль не должен совпадать с логином");
            return response;
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
            for (ConstraintViolation<UserDTO> violation : violations) {
                data.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            if (data.containsKey("email")) {
                response.setError(107);
                response.setDescription(data.get("email").toString());
                return response;
            } else if (data.containsKey("password")) {
                response.setError(108);
                response.setDescription(data.get("password").toString());
                return response;
            } else if (data.containsKey("phone")) {
                response.setError(111);
                response.setDescription(data.get("phone").toString());
                return response;
            }
        }

        int checkCode = getRandomIntegerBetweenRange(1000, 9999);

        String message = String.format("Hello! Your check code:\n %s", checkCode);

        mailSender.send(email, "Activation code", message);

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
        user.setRoles(new ArrayList<>());
        user.addRole(roleName);

        userRepository.save(user);*/

        return response;
    }

    @PostMapping("/reg-start")
    //(@Valid @RequestBody final UserDTO requestBody, BindingResult bindingResult)
    public RestResponse postCreateUser (@Valid @RequestBody UserDTO userDTO,
                                        BindingResult bindingResult) {

        final Roles roleNameDefault = rolesRepository.findByName("ROLE_USER");
        RestResponse response = new RestResponse();

        HashMap<String, Object> data = new HashMap<>();

        Optional<User> usr = userRepository.findByEmail(userDTO.getEmail());

        if (usr.isPresent()) {
            response.setError(106);
            response.setDescription("Пользователь существует");
            return response;
        }

        if (userDTO.getPassword().equals(userDTO.getEmail())) {
            response.setError(108);
            response.setDescription("Пароль не должен совпадать с логином");
            return response;
        }

        if (bindingResult.hasErrors()) {
            FieldError errors = bindingResult.getFieldError();
            /*response.put("requestDataUser", userDTO);
            response.put("errors", "Поле " + "'" + errors.getField() + "'" + " " + errors.getDefaultMessage());*/

            if (errors.getField().equals("email")) {
                response.setError(107);
                response.setDescription(errors.getDefaultMessage());
            } else if (errors.getField().equals("password")) {
                response.setError(108);
                response.setDescription(errors.getDefaultMessage());
            } else if (errors.getField().equals("phone")) {
                response.setError(111);
                response.setDescription(errors.getDefaultMessage());
            }

            return response;
        }

        int checkCode = getRandomIntegerBetweenRange(1000, 9999);

        String message = String.format("Hello! Your check code:\n %s", checkCode);

        mailSender.send(userDTO.getEmail(), "Activation code", message);

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
        user.setRoles(new ArrayList<>());
        user.addRole(roleNameDefault);

        userRepository.save(user);*/

        return response;
    }

    private static int getRandomIntegerBetweenRange(int min, int max){
        int x = (int) (Math.random() * ( (max - min) + 1 )) + min;
        return x;
    }
}
