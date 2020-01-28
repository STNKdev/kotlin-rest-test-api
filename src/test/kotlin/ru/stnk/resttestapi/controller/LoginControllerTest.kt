package ru.stnk.resttestapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.stnk.resttestapi.service.login.LoginForm


@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class LoginControllerTest (
        @Autowired val mockMvc: MockMvc
) {

    private val objectMapper = ObjectMapper()

    @Test
    @Throws(Exception::class)
    fun authenticateUserTest () {
        val loginFormPayload = LoginForm()
        loginFormPayload.email = "admin@test.io"
        loginFormPayload.password = "123"

        //val fields = ConstrainedFields(LoginForm::class.java)

        val res = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginFormPayload))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.api-key").value(Matchers.isA<Any>(String::class.java)))
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("email")
                                        .description("Email пользователя")
                                        .attributes(
                                                Attributes.key("constraints")
                                                        .value("Не должно быть пустым. Проверяется на корректность.")),
                                PayloadDocumentation.fieldWithPath("password")
                                        .description("Пароль пользователя")
                                        .attributes(
                                                Attributes.key("constraints")
                                                        .value("Не должно быть пустым. Минимальная длина 3 символа."))
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error")
                                        .description("Содержит код ошибки"),
                                PayloadDocumentation.fieldWithPath("description")
                                        .description("Содержит описание ошибки"),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data['api-key']")
                                        .description("Содержит api-key, который нужно вставлять в заголовок '-X-api-key' при последующих запросах.")
                        )
                )).andReturn()
        val apiKey: String? = objectMapper.readTree( res.response.contentAsString ).get("data").get("api-key").asText()

        if (!apiKey.isNullOrBlank()) {
            mockMvc.perform(MockMvcRequestBuilders.get("/userinfo")
                    .header("-X-api-key", apiKey)
            )
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                    .andDo(MockMvcRestDocumentation.document("{method-name}",
                            HeaderDocumentation.requestHeaders(
                                    HeaderDocumentation.headerWithName("-X-api-key")
                                            .description("Содержит уникальный ключ для аутентификации запросов")
                            ),
                            PayloadDocumentation.responseFields(
                                    PayloadDocumentation.fieldWithPath("error").ignored(),
                                    PayloadDocumentation.fieldWithPath("description").ignored(),
                                    PayloadDocumentation.subsectionWithPath("data")
                                            .description("Содержит данные запроса"),
                                    PayloadDocumentation.fieldWithPath("data['email']")
                                            .description("Email пользователя"),
                                    PayloadDocumentation.fieldWithPath("data['phone']")
                                            .description("Номер телефона пользователя"),
                                    PayloadDocumentation.fieldWithPath("data['emailConfirmed']")
                                            .description("Подтвержден ли Email пользователя"),
                                    PayloadDocumentation.fieldWithPath("data['freeBalance']")
                                            .description("Баланс пользователя"),
                                    PayloadDocumentation.fieldWithPath("data['betBalance']")
                                            .description("Баланс пользователя"),
                                    PayloadDocumentation.fieldWithPath("data['withdrawalBalance']")
                                            .description("Баланс пользователя")
                            )
                    ))
        }
    }


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