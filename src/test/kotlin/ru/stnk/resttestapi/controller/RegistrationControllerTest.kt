package ru.stnk.resttestapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.junit.Before
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
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestParametersSnippet
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.stnk.resttestapi.entity.Role
import ru.stnk.resttestapi.service.login.UserLoginForm
import ru.stnk.resttestapi.entity.RoleName
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
    /*@MockBean
    val userRepository: UserRepository? = null*/

    private val objectMapper = ObjectMapper()

    private val requestDocumentationParameters: RequestParametersSnippet = RequestDocumentation.requestParameters(
            RequestDocumentation.parameterWithName("email")
                    .description("Email пользователя")
                    .attributes(
                            key("constraints")
                                    .value("Не должно быть пустым. Проверяется на корректность.")),
            RequestDocumentation.parameterWithName("password")
                    .description("Пароль пользователя")
                    .attributes(
                            key("constraints")
                                    .value("Не должно быть пустым. Минимальная длина 3 символа.")),
            RequestDocumentation.parameterWithName("phone")
                    .description("Номер телефона пользователя")
                    .attributes(
                            key("constraints")
                                    .value("Не должно быть пустым. Проверяется на корректность.")),
            RequestDocumentation.parameterWithName("os")
                    .description("Используемая система пользователя")
                    .attributes(
                            key("constraints")
                                    .value("Не должно быть пустым.")),
            RequestDocumentation.parameterWithName("viaEmail").optional()
                    .description("Отправлять ли письмо на указанный Email")
                    .attributes(
                            key("constraints")
                                    .value("Не обязательно. По умолчанию true.")),
            RequestDocumentation.parameterWithName("code").optional()
                    .description("Код подтверждения для регистрации, который приходит на указанный email")
                    .attributes(
                            key("constraints")
                                    .value("Не должно быть пустым."))
    )

    private val payloadDocumentationRequestFields: RequestFieldsSnippet = PayloadDocumentation.requestFields(
            PayloadDocumentation.fieldWithPath("email")
                    .description("Email пользователя")
                    .attributes(
                            key("constraints")
                                    .value("Не должно быть пустым. Проверяется на корректность.")),
            PayloadDocumentation.fieldWithPath("password")
                    .description("Пароль пользователя")
                    .attributes(
                            key("constraints")
                                    .value("Не должно быть пустым. Минимальная длина 3 символа.")),
            PayloadDocumentation.fieldWithPath("phone")
                    .description("Номер телефона пользователя")
                    .attributes(
                            key("constraints")
                                    .value("Не должно быть пустым. Проверяется на корректность.")),
            PayloadDocumentation.fieldWithPath("os")
                    .description("Используемая система пользователя")
                    .attributes(
                            key("constraints")
                                    .value("Не должно быть пустым.")),
            PayloadDocumentation.fieldWithPath("viaEmail").optional()
                    .description("Отправлять ли письмо на указанный Email")
                    .attributes(
                            key("constraints")
                                    .value("Не обязательно. По умолчанию true.")),
            PayloadDocumentation.fieldWithPath("code").optional()
                    .description("Код подтверждения для регистрации, который приходит на указанный email")
                    .attributes(
                            key("constraints")
                                    .value("Не должно быть пустым."))
    )

    // Выполняет код перед каждым тестом
    /*@Before
    fun init() {}*/

    // Тест GET метода для получения кода подтверждения регистрации
    @Test
    @Order(1)
    @Throws(Exception::class)
    fun preRegistrationGetMethod() {

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
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        requestDocumentationParameters,
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
        //verify(registrationControllerService, times(1)).saveCheckCodeToEmail(anyString(), anyBoolean());
    }

    // Тест POST метода для получения кода подтверждения регистрации
    @Test
    @Order(3)
    @Throws(Exception::class)
    fun preRegistrationPostMethod() {

        val userLoginForm = UserLoginForm()
        userLoginForm.email = "user2@test.io"
        userLoginForm.password = "123"
        userLoginForm.phone = "88002000602"
        userLoginForm.os = "android"
        userLoginForm.isViaEmail = false

        //val fields = ConstrainedFields(UserLoginForm::class.java)

        mockMvc.perform(MockMvcRequestBuilders.post("/reg-start")
                .content(objectMapper.writeValueAsString(userLoginForm))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.secondsUntilExpired", Matchers.isA<Any>(Int::class.java)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.secondsUntilResend", Matchers.isA<Any>(Int::class.java)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.attempts", Matchers.isA<Any>(Int::class.java)))
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        // PayloadDocumentation.fieldWithPath
                        payloadDocumentationRequestFields,
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

    // Тест GET метода для регистрации пользователя и получения api-key
    @Test
    @Order(2)
    @Throws(Exception::class)
    fun registrationConfirmGetMethod() {

        val mockUser = User()
        mockUser.email = "user1@test.io"
        mockUser.phone = "88002000601"
        mockUser.password = "123"
        mockUser.os = "android"
        mockUser.isEnabled = true
        mockUser.emailConfirmed = true
        mockUser.id = 999L
        mockUser.roles.add( Role(RoleName.ROLE_USER) )

        // Используем when().thenReturn(), чтобы при попытке сохранить пользователя нам вернули нашего пользователя
        // иначе будут ошибки ( repository.save(user) must not be null )
        //Mockito.`when`( userRepository?.save( ArgumentMatchers.any(User::class.java) ) ).thenReturn(mockUser)

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.api-key", Matchers.isA<Any>(String::class.java)))
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        requestDocumentationParameters,
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error")
                                        .description("Содержит код ошибки"),
                                PayloadDocumentation.fieldWithPath("description")
                                        .description("Содержит описание ошибки"),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data['api-key']")
                                        .description("Содержит api-key, который нужно хранить надёжно. Действителен 3 дня.")
                        )
                ))

        //Mockito.verify(userRepository, VerificationModeFactory.times(1))?.save(ArgumentMatchers.any(User::class.java))
        //verify(verificationCodeRepository, times(1)).delete(any(VerificationCode.class));
    }

    // Тест POST метода для регистрации пользователя и получения api-key
    @Test
    @Order(4)
    @Throws(Exception::class)
    fun registrationConfirmPostMethod() {

        // Пользователь для POST запроса, который преобразуется в JSON
        val mockUserLoginForm = UserLoginForm()
        mockUserLoginForm.email = "user2@test.io"
        mockUserLoginForm.password = "123"
        mockUserLoginForm.phone = "88002000602"
        mockUserLoginForm.os = "android"
        mockUserLoginForm.isViaEmail = false
        mockUserLoginForm.code = "9999"

        // Пользователь для возврата из базы в подмененном userRepository
        val mockUser = User()
        mockUser.email = "user1@test.io"
        mockUser.phone = "88002000601"
        mockUser.password = "123"
        mockUser.os = "android"
        mockUser.isEnabled = true
        mockUser.emailConfirmed = true
        mockUser.roles.add( Role(RoleName.ROLE_USER) )

        // Устанавливаем ловушку для возврата нашего пользователя
        //Mockito.`when`( userRepository?.save( ArgumentMatchers.any(User::class.java) ) ).thenReturn(mockUser)

        mockMvc.perform(MockMvcRequestBuilders.post("/reg-confirm")
                .content(objectMapper.writeValueAsString(mockUserLoginForm))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.api-key", Matchers.isA<Any>(String::class.java)))
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        payloadDocumentationRequestFields,
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error")
                                        .description("Содержит код ошибки"),
                                PayloadDocumentation.fieldWithPath("description")
                                        .description("Содержит описание ошибки"),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data['api-key']")
                                        .description("Содержит api-key, который нужно хранить надёжно. Действителен 3 дня.")
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

    /*private class ConstrainedFields (input: Class<*>) {

        val constraintDescriptions: ConstraintDescriptions = ConstraintDescriptions(input)

        fun withPath(path: String): FieldDescriptor {
            return PayloadDocumentation.fieldWithPath(path)
                    .attributes(
                            Attributes.key("constraints")
                                    .value(
                                            StringUtils.collectionToDelimitedString(
                                                    constraintDescriptions.descriptionsForProperty(path), ". "
                                            )
                                    )
                    )
        }

    }*/


}