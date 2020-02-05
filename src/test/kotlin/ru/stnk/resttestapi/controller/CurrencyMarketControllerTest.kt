package ru.stnk.resttestapi.controller

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class CurrencyMarketControllerTest (
        @Autowired val mockMvc: MockMvc
) {

    @Test
    fun getCoins () {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/coins")
                .contextPath("/api")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error").ignored(),
                                PayloadDocumentation.fieldWithPath("description").ignored(),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data[0]['symbolName']")
                                        .description("Краткое имя валютной пары"),
                                PayloadDocumentation.fieldWithPath("data[0]['highPrice']")
                                        .description("Максимальный прайс"),
                                PayloadDocumentation.fieldWithPath("data[0]['lowPrice']")
                                        .description("Минимальный прайс"),
                                PayloadDocumentation.fieldWithPath("data[0]['lastPrice']")
                                        .description("Актуальный прайс"),
                                PayloadDocumentation.fieldWithPath("data[0]['bidPrice']")
                                        .description("Цена предложения"),
                                PayloadDocumentation.fieldWithPath("data[0]['askPrice']")
                                        .description("Цена спроса")
                        )
                ))
    }
}