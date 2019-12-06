package ru.stnk.RestTestAPI.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.stnk.RestTestAPI.entity.VerificationCode;
import ru.stnk.RestTestAPI.repository.VerificationCodeRepository;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerErrorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

//    @MockBean
//    private UserRepository userRepository;

//    @InjectMocks
//    private ControllerService controllerService;

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

        /*
        * Перед этим нужно добавить в таблицу user_verification_code запись с попытками,
        * иначе вернется пустой ответ и тест провалится
        * */

        VerificationCode verificationCode = new VerificationCode(1234,
                                                        "admin@test.io",
                                                                    60,
                                                                    Instant.now().plusSeconds(300));

        verificationCodeRepository.save(verificationCode);

        // С не mock object в этих местах ошибки
        //when(verificationCodeRepository.save(verificationCode)).thenReturn(verificationCode);

        //verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));

        this.mockMvc.perform(get("/reg-confirm")
                                .param("email", "admin@test.io")
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

        // И стираем запись обязательно
        // а по логам сам стирает
        //verificationCodeRepository.deleteById(verificationCode.getId());
    }
}
