package ru.stnk.RestTestAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import ru.stnk.RestTestAPI.results.RestResponse;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CurrencyMarketController {

    /*WebSocketClient client = new StandardWebSocketClient();
    WebSocketStompClient stompClient = new WebSocketStompClient(client);


    {
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    public RestResponse getInstruments() {
        String url = "wss://www.bitmex.com/realtime";
        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect(url, sessionHandler);
    }


    public class MyStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe();
            session.disconnect();
        }
    }*/

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/quotes")
    public RestResponse getQuotes () {

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

// Add the Jackson Message converter
        messageConverters.add(new MappingJackson2MessageConverter());

// Add the message converters to the restTemplate
        restTemplate.setMessageConverters(messageConverters);

        String url = "https://aappapi.investing.com/get_screen.php?screen_ID=5&v2=1&include_alert_counter=1&time_utc_offset=28800&lang_ID=7&skinID=2";

        RestResponse restResponse = new RestResponse();

        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-os", "Android");
        headers.set("x-meta-ver", "14");
        headers.set("x-app-ver", "1142");
        headers.set("x-udid", "7091021461366746");
        headers.set("User-Agent", "Android");
        headers.set("Content-Type", "application/json");
        headers.set("ccode", "RU");
        headers.set("ccode_time", "1562913602");
        headers.set("Host", "aappapi.investing.com");
        headers.set("Connection", "Keep-Alive");
        headers.set("Accept-Encoding", "gzip");
        headers.set("apf_id", "1562913512115-1492646621599829077");
        headers.set("apf_src", "org");

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<ObjectMapper> result = restTemplate.exchange(url, HttpMethod.GET, entity, ObjectMapper.class);

        restResponse.setData(result);

        return restResponse;
    }
}
