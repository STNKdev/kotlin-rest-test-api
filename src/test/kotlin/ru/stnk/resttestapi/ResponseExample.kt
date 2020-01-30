package ru.stnk.resttestapi

import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.stnk.resttestapi.controller.RegistrationController
import ru.stnk.resttestapi.results.RestResponse
import ru.stnk.resttestapi.service.RegistrationControllerService
import java.util.HashMap


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class ResponseExample (
        @Autowired val mockMvc: MockMvc
) {

    @MockBean
    var registrationControllerService: RegistrationControllerService? = null

    /*@MockBean
    var registrationController: RegistrationController? = null*/

    @Test
    fun responseEmptyExample() {

        val data: MutableMap<String, Any>? = HashMap()

        Mockito.`when`( registrationControllerService?.saveCheckCodeToEmail(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean()
        ) ).thenReturn( data )

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reg-confirm")
                .contextPath("/api")
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
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error")
                                        .description("Содержит код ошибки"),
                                PayloadDocumentation.fieldWithPath("description")
                                        .description("Содержит описание ошибки"),
                                PayloadDocumentation.fieldWithPath("data")
                                        .description("Содержит данные запроса")
                        )
                ))
    }
}