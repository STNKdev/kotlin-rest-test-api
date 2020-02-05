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
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.snippet.Attributes
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

    @Test
    fun getCandlesOne() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/candles-one")
                .contextPath("/api")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        RequestDocumentation.requestParameters(
                                RequestDocumentation.parameterWithName("symbol").optional()
                                        .description("Краткое имя валютной пары, если не указывать, то в ответе будут записи по всем доступным валютным парам")
                                        .attributes(
                                                Attributes.key("constraints")
                                                        .value("Необязательный параметр"))
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error").ignored(),
                                PayloadDocumentation.fieldWithPath("description").ignored(),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data[0]['symbolName']")
                                        .description("Краткое имя валютной пары"),
                                PayloadDocumentation.fieldWithPath("data[0]['bidPrice']")
                                        .description("Цена предложения"),
                                PayloadDocumentation.fieldWithPath("data[0]['askPrice']")
                                        .description("Цена спроса"),
                                PayloadDocumentation.fieldWithPath("data[0]['timestamp']")
                                        .description("Время обновления")
                        )
                ))
    }

    @Test
    fun getCandlesFive() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/candles-five")
                .contextPath("/api")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        RequestDocumentation.requestParameters(
                                RequestDocumentation.parameterWithName("symbol").optional()
                                        .description("Краткое имя валютной пары, если не указывать, то в ответе будут записи по всем доступным валютным парам")
                                        .attributes(
                                                Attributes.key("constraints")
                                                        .value("Необязательный параметр"))
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error").ignored(),
                                PayloadDocumentation.fieldWithPath("description").ignored(),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data[0]['symbolName']")
                                        .description("Краткое имя валютной пары"),
                                PayloadDocumentation.fieldWithPath("data[0]['bidPrice']")
                                        .description("Цена предложения"),
                                PayloadDocumentation.fieldWithPath("data[0]['askPrice']")
                                        .description("Цена спроса"),
                                PayloadDocumentation.fieldWithPath("data[0]['timestamp']")
                                        .description("Время обновления в формате ISO 8601")
                        )
                ))
    }

    @Test
    fun getCandlesOneWithParam() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/candles-one")
                .contextPath("/api")
                .param("symbol", "XBTUSD")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error").ignored(),
                                PayloadDocumentation.fieldWithPath("description").ignored(),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data[0]['symbolName']")
                                        .description("Краткое имя валютной пары"),
                                PayloadDocumentation.fieldWithPath("data[0]['bidPrice']")
                                        .description("Цена предложения"),
                                PayloadDocumentation.fieldWithPath("data[0]['askPrice']")
                                        .description("Цена спроса"),
                                PayloadDocumentation.fieldWithPath("data[0]['timestamp']")
                                        .description("Время обновления в формате ISO 8601")
                        )
                ))
    }

    @Test
    fun getCandlesFiveWithParam() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/candles-five")
                .contextPath("/api")
                .param("symbol", "XBTUSD")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcRestDocumentation.document("{method-name}",
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("error").ignored(),
                                PayloadDocumentation.fieldWithPath("description").ignored(),
                                PayloadDocumentation.subsectionWithPath("data")
                                        .description("Содержит данные запроса"),
                                PayloadDocumentation.fieldWithPath("data[0]['symbolName']")
                                        .description("Краткое имя валютной пары"),
                                PayloadDocumentation.fieldWithPath("data[0]['bidPrice']")
                                        .description("Цена предложения"),
                                PayloadDocumentation.fieldWithPath("data[0]['askPrice']")
                                        .description("Цена спроса"),
                                PayloadDocumentation.fieldWithPath("data[0]['timestamp']")
                                        .description("Время обновления в формате ISO 8601")
                        )
                ))
    }
}