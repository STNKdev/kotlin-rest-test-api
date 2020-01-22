package ru.stnk.resttestapi.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.stnk.resttestapi.entity.VerificationCode
import ru.stnk.resttestapi.repository.VerificationCodeRepository
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated/documentation")
class RegistrationControllerErrorTest (
        @Autowired val mockMvc: MockMvc
        //@Autowired val verificationCodeRepository: VerificationCodeRepository
) {
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
    @MockBean
    val verificationCodeRepository: VerificationCodeRepository? = null

    val description: List<FieldDescriptor> = listOf(
            PayloadDocumentation.fieldWithPath("error")
                    .description("Содержит код ошибки"),
            PayloadDocumentation.fieldWithPath("description")
                    .description("Содержит описание ошибки")
    )

    // Проверка неккоректного email
    @Test
    @Throws(Exception::class)
    fun notCorrectEmailError() {
        mockMvc.perform(MockMvcRequestBuilders.get("/reg-start")
                .param("email", "user1_test.io")
                .param("password", "123")
                .param("phone", "88002000601")
                .param("os", "android")
                .param("viaEmail", "false")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.`is`(107)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.`is`("Некорректный email")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist())
                .andDo(MockMvcRestDocumentation.document("{methodName}",
                        PayloadDocumentation.responseFields(description)
                ))
    }

    // Проверка совпадения логина с паролем
    @Test
    @Throws(Exception::class)
    fun equalLoginPasswordError() {
        mockMvc.perform(MockMvcRequestBuilders.get("/reg-start")
                .param("email", "user1@test.io")
                .param("password", "user1@test.io")
                .param("phone", "88002000601")
                .param("os", "android")
                .param("viaEmail", "false")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.`is`(108)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.`is`("Пароль не должен совпадать с логином")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist())
                .andDo(MockMvcRestDocumentation.document("{methodName}",
                        PayloadDocumentation.responseFields(description)
                ))
    }

    // Проверка формата номера телефона
    @Test
    @Throws(Exception::class)
    fun checkNumberPhoneError() {
        mockMvc.perform(MockMvcRequestBuilders.get("/reg-start")
                .param("email", "user1@test.io")
                .param("password", "123")
                .param("phone", "+8(495) 302-66-88")
                .param("os", "android")
                .param("viaEmail", "false")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.`is`(111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.`is`("Некорректный номер телефона")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist())
                .andDo(MockMvcRestDocumentation.document("{methodName}",
                        PayloadDocumentation.responseFields(description)
                ))
    }

    // Проверка на существование такого же пользователя
    @Test
    @Throws(Exception::class)
    fun userExistError() {

        val userEmail = "admin@test.io"

        val verificationCode = VerificationCode(1234,
                userEmail,
                60,
                Instant.now().plusSeconds(300))

        Mockito.`when`( verificationCodeRepository?.findByUserEmail(userEmail) ).thenReturn( Optional.of(verificationCode) )
        //Mockito.`when`( verificationCodeRepository?.save( ArgumentMatchers.any(VerificationCode::class.java) ) ).thenReturn(verificationCode)

        mockMvc.perform(MockMvcRequestBuilders.get("/reg-confirm")
                .param("email", userEmail)
                .param("password", "123")
                .param("phone", "88002000600")
                .param("os", "android")
                .param("viaEmail", "false")
                .param("code", "9999")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.`is`(106)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.`is`("Пользователь существует")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist())
                .andDo(MockMvcRestDocumentation.document("{methodName}",
                        PayloadDocumentation.responseFields(description)
                ))

        Mockito.verify(verificationCodeRepository, Mockito.times(1))?.findByUserEmail(userEmail)
        Mockito.verify(verificationCodeRepository, Mockito.times(0))?.save(verificationCode)

    }
}