package ru.stnk.RestTestAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stnk.RestTestAPI.results.RestResponse;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ServerInformationController {
    @GetMapping("/currtime")
    public RestResponse currtime () {
        RestResponse restResponse = new RestResponse();
        Instant instant = Instant.now();
        //LocalDateTime localDateTime = LocalDateTime.now();
        Map<String, Object> data = new HashMap<>();
        data.put("time", instant.getEpochSecond());
        data.put("timeString", instant.atZone(ZoneId.of("Europe/Moscow")));

        restResponse.setData(data);

        return restResponse;
    }
}
