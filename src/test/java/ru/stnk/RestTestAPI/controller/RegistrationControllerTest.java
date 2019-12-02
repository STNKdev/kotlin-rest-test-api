package ru.stnk.RestTestAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RolesRepository rolesRepository;

    @MockBean
    private VerificationCodeRepository verificationCodeRepository;

    /*@Before
    public void init() {
        rolesRepository.save(new Roles("ROLE_ADMIN"));
    }*/

    @Test
    public void preRegistrationGetMethodTest() throws Exception {

        /*
        User user = new User();
        user.setEmail("admin@test.io");
        user.setPassword("123");
        user.setPhone("88002000600");
        user.setOs("android");
        user.setEnabled(true);
        user.setEmailConfirmed(true);
        user.setBetBalance((long) 0);
        user.setFreeBalance((long) 0);
        user.setWithdrawalBalance((long) 0);
        user.setRoles(new ArrayList<>());
        user.addRole(rolesRepository.findByName("ROLE_USER"));

        when(userRepository.save(any(User.class))).thenReturn(user);
        */

        this.mockMvc.perform(get("/reg-start")
                                .param("email", "admin1@test.io")
                                .param("password", "123")
                                .param("phone", "88002000601")
                                .param("os", "android")
                                .param("viaEmail", "false")
                            )
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error", is(0)))
                .andExpect(jsonPath("$.description", is("")))
                .andExpect(jsonPath("$.data.secondsUntilExpired", Matchers.isA(Integer.class)))
                .andExpect(jsonPath("$.data.secondsUntilResend", Matchers.isA(Integer.class)))
                .andExpect(jsonPath("$.data.attempts", Matchers.isA(Integer.class)));

        verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));

    }

    @Test
    public void preRegistrationPostMethodTest() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("admin2@test.io");
        userDTO.setPassword("123");
        userDTO.setPhone("88002000602");
        userDTO.setOs("android");
        userDTO.setViaEmail(false);

        this.mockMvc.perform(post("/reg-start")
                                .content(objectMapper.writeValueAsString(userDTO))
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error", is(0)))
                .andExpect(jsonPath("$.description", is("")))
                .andExpect(jsonPath("$.data.secondsUntilExpired", Matchers.isA(Integer.class)))
                .andExpect(jsonPath("$.data.secondsUntilResend", Matchers.isA(Integer.class)))
                .andExpect(jsonPath("$.data.attempts", Matchers.isA(Integer.class)));

        verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));
    }

    @Test
    public void registrationConfirmGetMethodTest() throws Exception {

        this.mockMvc.perform(get("/reg-start")
                .param("email", "admin1@test.io")
                .param("password", "123")
                .param("phone", "88002000601")
                .param("os", "android")
                .param("code", "9999")
                .param("viaEmail", "false")
        )
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error", is(0)))
                .andExpect(jsonPath("$.description", is("")))
                .andExpect(jsonPath("$.data.secondsUntilExpired", Matchers.isA(Integer.class)))
                .andExpect(jsonPath("$.data.secondsUntilResend", Matchers.isA(Integer.class)))
                .andExpect(jsonPath("$.data.attempts", Matchers.isA(Integer.class)));

        verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));

    }
}
