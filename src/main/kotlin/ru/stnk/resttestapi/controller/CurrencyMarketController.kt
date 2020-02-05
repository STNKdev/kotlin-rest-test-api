package ru.stnk.resttestapi.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.stnk.resttestapi.entity.coins.InstrumentEntity
import ru.stnk.resttestapi.entity.coins.QuoteBinOneMinute
import ru.stnk.resttestapi.repository.InstrumentEntityRepository
import ru.stnk.resttestapi.repository.QuoteBinFiveMinuteRepository
import ru.stnk.resttestapi.repository.QuoteBinOneMinuteRepository
import ru.stnk.resttestapi.results.RestResponse

@RestController
class CurrencyMarketController (
        @Autowired val instrumentEntityRepository: InstrumentEntityRepository,
        @Autowired val quoteBinOneMinuteRepository: QuoteBinOneMinuteRepository,
        @Autowired val quoteBinFiveMinuteRepository: QuoteBinFiveMinuteRepository
) {

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

    private val xbtUsd = "XBTUSD"
    private val ethUsd = "ETHUSD"

    /*//получить все пары валюты
    val data: ResponseEntity
        @GetMapping("/coins")
        get() {
            val instrumentEntity = instrumentEntityRepository!!.findAll()
            return ResponseEntity(instrumentEntity, HttpStatus.OK)
        }
    */

    //получить все пары валюты
    @GetMapping("/coins")
    fun getCoins (): RestResponse {
        val restResponse: RestResponse = RestResponse()
        val instrumentEntity: List<InstrumentEntity> = instrumentEntityRepository.findAll()
        restResponse.data = instrumentEntity
        return restResponse
    }

    //получить котировки в минутных свечах
    @GetMapping("/candles-one")
    fun getCandlesOne(@RequestParam(required = false) symbol: String?): RestResponse {

        val restResponse: RestResponse = RestResponse()

        // Если параметр symbol не пустой, то возвращаем список котировок определенной валютной пары
        if (symbol != null) {
            if (symbol.equals(xbtUsd, ignoreCase = true)) {
                val quoteBinOneMinutes: List<QuoteBinOneMinute>? = quoteBinOneMinuteRepository.findBySymbolNameOrderByTimestampDesc(xbtUsd)
                restResponse.data = quoteBinOneMinutes
                return restResponse
            }
            if (symbol.equals(ethUsd, ignoreCase = true)) {
                val quoteBinOneMinutes: List<QuoteBinOneMinute>? = quoteBinOneMinuteRepository.findBySymbolNameOrderByTimestampDesc(ethUsd)
                restResponse.data = quoteBinOneMinutes
                return restResponse
            }
        }

        // Если параметр symbol пустой, то возвращаем список всех доступных котировок
        val quoteBinOneMinutes = quoteBinOneMinuteRepository.findAll()
        restResponse.data = quoteBinOneMinutes
        return restResponse

    }

    //получить котировки в пятиминутных свечах
    @GetMapping("/candles-five")
    fun getCandlesFive(@RequestParam(required = false) symbol: String?): RestResponse {

        val restResponse: RestResponse = RestResponse()

        // Если параметр symbol не пустой, то возвращаем список котировок определенной валютной пары
        if (symbol != null) {
            if (symbol.equals(xbtUsd, ignoreCase = true)) {
                val quoteBinFiveMinutes = quoteBinFiveMinuteRepository.findBySymbolNameOrderByTimestampDesc(xbtUsd)
                restResponse.data = quoteBinFiveMinutes
                return restResponse
            }
            if (symbol.equals(ethUsd, ignoreCase = true)) {
                val quoteBinFiveMinutes = quoteBinFiveMinuteRepository.findBySymbolNameOrderByTimestampDesc(ethUsd)
                restResponse.data = quoteBinFiveMinutes
                return restResponse
            }
        }

        // Если параметр symbol пустой, то возвращаем список всех доступных котировок
        val quoteBinFiveMinutes = quoteBinFiveMinuteRepository.findAll()
        restResponse.data = quoteBinFiveMinutes
        return restResponse
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
