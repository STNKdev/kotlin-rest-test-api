package ru.stnk.resttestapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.stnk.resttestapi.dto.UserDTO
import ru.stnk.resttestapi.entity.Roles
import ru.stnk.resttestapi.entity.User
import ru.stnk.resttestapi.repository.UserRepository

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@AutoConfigureRestDocs
class RegistrationControllerTest (
        @Autowired val mockMvc: MockMvc
) {

    // Подменяем репозиторий, чтобы в базу не ходил и не создавал нам там пользователей
    @MockBean
    val userRepository: UserRepository? = null

    private val objectMapper = ObjectMapper()

    // Выполняет код перед каждым тестом
    /*@Before
        public void init() {
            rolesRepository.save(new Roles("ROLE_ADMIN"));
        }*/
    // Тест GET метода для получения кода подтверждения регистрации
    @Test
    @Order(1)
    @Throws(Exception::class)
    fun preRegistrationGetMethodTest() {

        mockMvc.perform(MockMvcRequestBuilders.get("/reg-start")
                .param("email", "user1@test.io")
                .param("password", "123")
                .param("phone", "88002000601")
                .param("os", "android")
                .param("viaEmail", "false")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.secondsUntilExpired", Matchers.isA<Any>(Int::class.java)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.secondsUntilResend", Matchers.isA<Any>(Int::class.java)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.attempts", Matchers.isA<Any>(Int::class.java)))
                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",
                        RequestDocumentation.requestParameters(
                            RequestDocumentation.parameterWithName("email")
                                    .description("Email пользователя"),
                            RequestDocumentation.parameterWithName("password")
                                    .description("Пароль пользователя"),
                            RequestDocumentation.parameterWithName("phone")
                                    .description("Номер телефона пользователя"),
                            RequestDocumentation.parameterWithName("os")
                                    .description("Используемая система пользователя"),
                            RequestDocumentation.parameterWithName("viaEmail")
                                    .description("Отправлять ли письмо на указанный Email").optional()
                        ),
                        PayloadDocumentation.responseFields(
                            PayloadDocumentation.fieldWithPath("error")
                                    .description("Содержит код ошибки"),
                            PayloadDocumentation.fieldWithPath("description")
                                    .description("Содержит описание ошибки"),
                            PayloadDocumentation.subsectionWithPath("data")
                                    .description("Содержит данные запроса"),
                            PayloadDocumentation.fieldWithPath("data['secondsUntilExpired']")
                                    .description("Время, через которое истечет проверочный код"),
                            PayloadDocumentation.fieldWithPath("data['secondsUntilResend']")
                                    .description("Время, для повторного запроса проверочного кода"),
                            PayloadDocumentation.fieldWithPath("data['attempts']")
                                    .description("Число попыток для ввода проверочного кода")
                        )
                ))


        //verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));
        //verify(controllerService, times(1)).saveCheckCodeToEmail(anyString(), anyBoolean());
    }

    // Тест POST метода для получения кода подтверждения регистрации
    @Test
    @Order(3)
    @Throws(Exception::class)
    fun preRegistrationPostMethodTest() {

        val userDTO = UserDTO()
        userDTO.email = "user2@test.io"
        userDTO.password = "123"
        userDTO.phone = "88002000602"
        userDTO.os = "android"
        userDTO.isViaEmail = false

        mockMvc.perform(MockMvcRequestBuilders.post("/reg-start")
                .content(objectMapper.writeValueAsString(userDTO))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.secondsUntilExpired", Matchers.isA<Any>(Int::class.java)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.secondsUntilResend", Matchers.isA<Any>(Int::class.java)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.attempts", Matchers.isA<Any>(Int::class.java)))
                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("email")
                                        .description("Email пользователя"),
                                PayloadDocumentation.fieldWithPath("password")
                                        .description("Пароль пользователя"),
                                PayloadDocumentation.fieldWithPath("phone")
                                        .description("Номер телефона пользователя"),
                                PayloadDocumentation.fieldWithPath("os")
                                        .description("Используемая система пользователя"),
                                PayloadDocumentation.fieldWithPath("viaEmail")
                                        .description("Отправлять ли письмо на указанный Email").optional(),
                                PayloadDocumentation.fieldWithPath("code").ignored()
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error")
                                        .description("Содержит код ошибки"),
                                PayloadDocumentation.fieldWithPath("description")
                                        .description("Содержит описание ошибки"),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data['secondsUntilExpired']")
                                        .description("Время через которое истечет проверочный код"),
                                PayloadDocumentation.fieldWithPath("data['secondsUntilResend']")
                                        .description("Время для повторного запроса проверочного кода"),
                                PayloadDocumentation.fieldWithPath("data['attempts']")
                                        .description("Число попыток для ввода проверочного кода")
                        )
                ))


        //verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));
        //verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));
    }

    // Тест GET метода для регистрации пользователя и получения Session_id
    @Test
    @Order(2)
    @Throws(Exception::class)
    fun registrationConfirmGetMethodTest() {

        val mockUser = User()
        mockUser.email = "user1@test.io"
        mockUser.phone = "88002000601"
        mockUser.password = "123"
        mockUser.os = "android"
        mockUser.isEnabled = true
        mockUser.emailConfirmed = true
        mockUser.roles.add(Roles("ROLE_USER"))

        // Используем when().thenReturn(), чтобы при попытке сохранить пользователя нам вернули нашего пользователя
        // иначе будут ошибки ( repository.save(user) must not be null )
        Mockito.`when`( userRepository?.save( ArgumentMatchers.any(User::class.java) ) ).thenReturn(mockUser)

        mockMvc.perform(MockMvcRequestBuilders.get("/reg-confirm")
                .param("email", "user1@test.io")
                .param("password", "123")
                .param("phone", "88002000601")
                .param("os", "android")
                .param("code", "9999")
                .param("viaEmail", "false")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.session_id", Matchers.isA<Any>(String::class.java)))
                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",
                        RequestDocumentation.requestParameters(
                                RequestDocumentation.parameterWithName("email")
                                        .description("Email пользователя"),
                                RequestDocumentation.parameterWithName("password")
                                        .description("Пароль пользователя"),
                                RequestDocumentation.parameterWithName("phone")
                                        .description("Номер телефона пользователя"),
                                RequestDocumentation.parameterWithName("os")
                                        .description("Используемая система пользователя"),
                                RequestDocumentation.parameterWithName("viaEmail")
                                        .description("Отправлять ли письмо на указанный Email").optional(),
                                RequestDocumentation.parameterWithName("code")
                                        .description("Код подтверждения для регистрации, который приходит на указанный email")
                        ),
                        HeaderDocumentation.responseHeaders(
                                HeaderDocumentation.headerWithName("Set-Cookie")
                                        .description("Поле заголовка SESSION с ID авторизованной сессии, используется при последующих запросах")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error")
                                        .description("Содержит код ошибки"),
                                PayloadDocumentation.fieldWithPath("description")
                                        .description("Содержит описание ошибки"),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data['session_id']")
                                        .description("Содержит session_id, которую нужно сохранить, т.к. сессия будет хранится долго")
                        )
                ))


        //Mockito.verify(userRepository, VerificationModeFactory.times(1))?.save(ArgumentMatchers.any(User::class.java))
        //verify(verificationCodeRepository, times(1)).delete(any(VerificationCode.class));
    }

    // Тест POST метода для регистрации пользователя и получения Session_id
    @Test
    @Order(4)
    @Throws(Exception::class)
    fun registrationConfirmPostMethodTest() {

        // Пользователь для POST запроса, который преобразуется в JSON
        val mockUserDTO = UserDTO()
        mockUserDTO.email = "user2@test.io"
        mockUserDTO.password = "123"
        mockUserDTO.phone = "88002000602"
        mockUserDTO.os = "android"
        mockUserDTO.isViaEmail = false
        mockUserDTO.code = "9999"

        // Пользователь для возврата из базы в подмененном userRepository
        val mockUser = User()
        mockUser.email = "user1@test.io"
        mockUser.phone = "88002000601"
        mockUser.password = "123"
        mockUser.os = "android"
        mockUser.isEnabled = true
        mockUser.emailConfirmed = true
        mockUser.roles.add(Roles("ROLE_USER"))

        // Устанавливаем ловушку для возврата нашего пользователя
        Mockito.`when`( userRepository?.save( ArgumentMatchers.any(User::class.java) ) ).thenReturn(mockUser)

        mockMvc.perform(MockMvcRequestBuilders.post("/reg-confirm")
                .content(objectMapper.writeValueAsString(mockUserDTO))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.session_id", Matchers.isA<Any>(String::class.java)))
                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",
                        PayloadDocumentation.relaxedRequestFields(
                                PayloadDocumentation.fieldWithPath("email")
                                        .description("Email пользователя"),
                                PayloadDocumentation.fieldWithPath("password")
                                        .description("Пароль пользователя"),
                                PayloadDocumentation.fieldWithPath("phone")
                                        .description("Номер телефона пользователя"),
                                PayloadDocumentation.fieldWithPath("os")
                                        .description("Используемая система пользователя"),
                                PayloadDocumentation.fieldWithPath("viaEmail")
                                        .description("Отправлять ли письмо на указанный Email").optional(),
                                PayloadDocumentation.fieldWithPath("code")
                                        .description("Код подтверждения для регистрации, который приходит на указанный email")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error")
                                        .description("Содержит код ошибки"),
                                PayloadDocumentation.fieldWithPath("description")
                                        .description("Содержит описание ошибки"),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data['session_id']")
                                        .description("Содержит session_id, которую нужно сохранить, т.к. сессия будет хранится долго")
                        )
                ))


        //Mockito.verify(userRepository, VerificationModeFactory.times(1))!!.save(ArgumentMatchers.any(User::class.java))
        //verify(verificationCodeRepository, times(1)).delete(any(VerificationCode.class));
    }

    // Выполняет этот код после каждого теста
    /*@After
    public void cleanup() {
        if (userRepository.findByEmail("admin1@test.io").isPresent()) {
            userRepository.delete(userRepository.findByEmail("admin1@test.io").get());
        }

        if (userRepository.findByEmail("admin2@test.io").isPresent()) {
            userRepository.delete(userRepository.findByEmail("admin2@test.io").get());
        }

    }*/
}