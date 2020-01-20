package ru.stnk.resttestapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.stnk.resttestapi.results.RestResponse
import java.time.Instant
import java.time.ZoneId
import java.util.*

@RestController
class ServerInformationController {

    @GetMapping("/currtime")
    fun currtime(): RestResponse {
        val restResponse = RestResponse()
        val instant = Instant.now()
        //LocalDateTime localDateTime = LocalDateTime.now();
        val data = HashMap<String, Any>()
        data["time"] = instant.epochSecond
        data["timeString"] = instant.atZone(ZoneId.of("Europe/Moscow"))

        restResponse.data = data

        return restResponse
    }
}
