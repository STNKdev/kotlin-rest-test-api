package ru.stnk.RestTestAPI.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.stnk.RestTestAPI.dto.UserDTO;
import ru.stnk.RestTestAPI.entity.Roles;
import ru.stnk.RestTestAPI.entity.User;
import ru.stnk.RestTestAPI.entity.VerificationCode;
import ru.stnk.RestTestAPI.repository.RolesRepository;
import ru.stnk.RestTestAPI.repository.UserRepository;
import ru.stnk.RestTestAPI.repository.VerificationCodeRepository;
import ru.stnk.RestTestAPI.service.ControllerService;

import java.time.Instant;
import java.util.ArrayList;

import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerErrorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VerificationCodeRepository verificationCodeRepository;

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private ControllerService controllerService;

    /*
     * Тесты возможных регистрационных ошибок:
     * 106 - пользователь существует
     * 107 - неккоректный email
     * 108 - пароль не должен совпадать с логином
     * 111 - неккоректный формат номера телефона
     * 112 - слишком рано был запрошен повторный вызов кода подтверждения
     * */

    // Проверка неккоректного email
    @Test
    public void notCorrectEmailError() throws Exception {

        this.mockMvc.perform(get("/reg-start")
                                .param("email", "admin1_test.io")
                                .param("password", "123")
                                .param("phone", "88002000601")
                                .param("os", "android")
                                .param("viaEmail", "false")
                            )
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error", Matchers.is(107)))
                .andExpect(jsonPath("$.description", Matchers.is("Некорректный email")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    // Проверка совпадения логина с паролем
    @Test
    public void equalLoginPasswordError() throws Exception {

        this.mockMvc.perform(get("/reg-start")
                                .param("email", "admin1@test.io")
                                .param("password", "admin1@test.io")
                                .param("phone", "88002000601")
                                .param("os", "android")
                                .param("viaEmail", "false")
                            )
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error", Matchers.is(108)))
                .andExpect(jsonPath("$.description", Matchers.is("Пароль не должен совпадать с логином")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    // Проверка формата номера телефона
    @Test
    public void checkNumberPhoneError() throws Exception {

        this.mockMvc.perform(get("/reg-start")
                                .param("email", "admin1@test.io")
                                .param("password", "123")
                                .param("phone", "+8(495) 302-66-88")
                                .param("os", "android")
                                .param("viaEmail", "false")
                            )
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error", Matchers.is(111)))
                .andExpect(jsonPath("$.description", Matchers.is("Некорректный номер телефона")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    // Проверка на существование такого же пользователя
    @Test
    public void userExistError() throws Exception {

//        UserDTO userDTO = new UserDTO("admin1@test.io", "88002000600", "123");
//        userDTO.setViaEmail(false);
        Instant requestTime = Instant.now();

        VerificationCode verificationCode = new VerificationCode(1234, "admin1@test.io", 60, requestTime.plusSeconds(300));

        when(verificationCodeRepository.save(verificationCode)).thenReturn(verificationCode);

        verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));

        this.mockMvc.perform(get("/reg-confirm")
                                .param("email", "admin1@test.io")
                                .param("password", "123")
                                .param("phone", "88002000600")
                                .param("os", "android")
                                .param("viaEmail", "false")
                                .param("code", "9999")
                            )
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error", Matchers.is(106)))
                .andExpect(jsonPath("$.description", Matchers.is("Пользователь существует")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
