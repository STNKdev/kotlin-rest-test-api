package ru.stnk.RestTestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.stnk.RestTestAPI.entity.coins.InstrumentEntity;
import ru.stnk.RestTestAPI.entity.coins.QuoteBinFiveMinute;
import ru.stnk.RestTestAPI.entity.coins.QuoteBinOneMinute;
import ru.stnk.RestTestAPI.repository.InstrumentEntityRepository;
import ru.stnk.RestTestAPI.repository.QuoteBinFiveMinuteRepository;
import ru.stnk.RestTestAPI.repository.QuoteBinOneMinuteRepository;

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

    @Autowired
    private InstrumentEntityRepository instrumentEntityRepository;

    @Autowired
    private QuoteBinOneMinuteRepository quoteBinOneMinuteRepository;

    @Autowired
    private QuoteBinFiveMinuteRepository quoteBinFiveMinuteRepository;

    private final String xbtUsd = "XBTUSD";
    private final String ethUsd = "ETHUSD";

    //получить все пары валюты
    @GetMapping("/coins")
    public ResponseEntity getData () {
        List<InstrumentEntity> instrumentEntity = instrumentEntityRepository.findAll();
        return new ResponseEntity(instrumentEntity, HttpStatus.OK);
    }

    //получить котировки в минутных свечах
    @GetMapping("/candles-one")
    public ResponseEntity getCandlesOne (@RequestParam(required = false) String symbol) {
        if (symbol != null) {
            if (symbol.equalsIgnoreCase(xbtUsd)) {
                List<QuoteBinOneMinute> quoteBinOneMinutes = quoteBinOneMinuteRepository.findBySymbolNameOrderByTimestampDesc(xbtUsd);
                return new ResponseEntity(quoteBinOneMinutes, HttpStatus.OK);
            }
            if (symbol.equalsIgnoreCase(ethUsd)) {
                List<QuoteBinOneMinute> quoteBinOneMinutes = quoteBinOneMinuteRepository.findBySymbolNameOrderByTimestampDesc(ethUsd);
                return new ResponseEntity(quoteBinOneMinutes, HttpStatus.OK);
            }
        }

        List<QuoteBinOneMinute> quoteBinOneMinutes = quoteBinOneMinuteRepository.findAll();
        return new ResponseEntity(quoteBinOneMinutes, HttpStatus.OK);

    }

    //получить котировки в пятиминутных свечах
    @GetMapping("/candles-five")
    public ResponseEntity getCandlesFive (@RequestParam(required = false) String symbol) {
        if (symbol != null) {
            if (symbol.equalsIgnoreCase(xbtUsd)) {
                List<QuoteBinFiveMinute> quoteBinFiveMinutes = quoteBinFiveMinuteRepository.findBySymbolNameOrderByTimestampDesc(xbtUsd);
                return new ResponseEntity(quoteBinFiveMinutes, HttpStatus.OK);
            }
            if (symbol.equalsIgnoreCase(ethUsd)) {
                List<QuoteBinFiveMinute> quoteBinFiveMinutes = quoteBinFiveMinuteRepository.findBySymbolNameOrderByTimestampDesc(ethUsd);
                return new ResponseEntity(quoteBinFiveMinutes, HttpStatus.OK);
            }
        }

        List<QuoteBinFiveMinute> quoteBinFiveMinutes = quoteBinFiveMinuteRepository.findAll();
        return new ResponseEntity(quoteBinFiveMinutes, HttpStatus.OK);
    }

    /*private RestTemplate restTemplate = new RestTemplate();
    * спёр из приложения investing.com
    @GetMapping("/quotations")
    public RestResponse getQuotations () {

        String url = "https://aappapi.investing.com/get_screen.php?screen_ID=5&v2=1&include_alert_counter=1&time_utc_offset=28800&lang_ID=7&skinID=2";

        RestResponse restResponse = new RestResponse();

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
        headers.set("Accept-Encoding", "application/gzip");
        headers.set("apf_id", "1562913512115-1492646621599829077");
        headers.set("apf_src", "org");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<QuotationsResponse> result = restTemplate.exchange(url, HttpMethod.GET, entity, QuotationsResponse.class);

        QuotationsData[] data;
        try {
            data = result.getBody().getData();
        } catch (NullPointerException ex) {
            data = null;
        }

        restResponse.setData(data[0].getScreen_data().getPairs_data());

        return restResponse;
    }*/
}
