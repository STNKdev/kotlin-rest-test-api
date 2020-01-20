package ru.stnk.resttestapi.service

import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.WebSocketConnectionManager
import ru.stnk.resttestapi.entity.coins.InstrumentEntity
import ru.stnk.resttestapi.entity.coins.QuoteBinFiveMinute
import ru.stnk.resttestapi.entity.coins.QuoteBinOneMinute
import ru.stnk.resttestapi.repository.InstrumentEntityRepository
import ru.stnk.resttestapi.repository.QuoteBinFiveMinuteRepository
import ru.stnk.resttestapi.repository.QuoteBinOneMinuteRepository
import java.io.IOException
import java.time.Instant
import java.util.*

@Service
class SimpleWsHandler : WebSocketHandler {

    @Autowired
    private var instrumentEntityRepository: InstrumentEntityRepository? = null

    @Autowired
    private var quoteBinOneMinuteRepository: QuoteBinOneMinuteRepository? = null

    @Autowired
    private var quoteBinFiveMinuteRepository: QuoteBinFiveMinuteRepository? = null

    private val mapper = ObjectMapper()

    private val context: ApplicationContext? = null

    private val logger: Logger = LoggerFactory.getLogger(SimpleWsHandler::class.java)

    /**
     * Вызывается после websocket соединения с сервером.
     */
    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {

    }

    /**
     * Главный метод обработки серверных сообщений.
     */
    @Throws(Exception::class)
    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        //System.out.println(message.getPayload());

        saveMessage(message.payload)
    }

    /**
     * Обработчик ошибок.
     */
    @Throws(Exception::class)
    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {

    }

    /**
     * Вызывается когда WebSocket соединение закрывается.
     */
    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        logger.info("Соединение закрывается")
        logger.info("Попытка перезапустить соединение")
        val wsConnectionManager: WebSocketConnectionManager = context?.getBean("wsConnectionManager") as WebSocketConnectionManager
        wsConnectionManager.start()
        if (wsConnectionManager.isRunning) logger.info("Перезапуск соединения успешен")
    }

    override fun supportsPartialMessages(): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    private fun saveMessage(message: Any) {

        try {
            val root = mapper.readTree(message as String)

            val table = root.path("table").asText()
            val action = root.path("action").asText()

            //System.out.println("Значения table и partial: " + table + " " + action);

            if (table == "instrument" && action == "partial") {

                val data = root.path("data")

                if (data.isArray) {
                    for (curr in data) {
                        try {
                            /*instrumentEntityRepository.save(new InstrumentEntity(
                                    curr.path("symbol").asText(),
                                    Float.parseFloat(curr.path("highPrice").asText()),
                                    Float.parseFloat(curr.path("lowPrice").asText()),
                                    Float.parseFloat(curr.path("lastPrice").asText()),
                                    Float.parseFloat(curr.path("bidPrice").asText()),
                                    Float.parseFloat(curr.path("askPrice").asText())
                            ));*/
                            // В этом месте предупреждение, которое не даёт компилить
                            // Condition 'instrumentEntityRepository!!.findBySymbolName(curr.path("symbol").asText()) == null' is always 'false'

                            val instrument: InstrumentEntity? = instrumentEntityRepository?.findBySymbolName(curr.path("symbol").asText())

                            if (instrument == null) {
                                instrumentEntityRepository?.save(InstrumentEntity(
                                        curr.path("symbol").asText(),
                                        curr.path("highPrice").asDouble(),
                                        curr.path("lowPrice").asDouble(),
                                        curr.path("lastPrice").asDouble(),
                                        curr.path("bidPrice").asDouble(),
                                        curr.path("askPrice").asDouble()
                                ))
                            }

                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }

                    }
                }
            }

            if (table == "instrument" && action == "update") {

                val data = root.path("data")

                if (data.isArray) {
                    var symbol: String?
                    var highPrice: Double?
                    var lowPrice: Double?
                    var lastPrice: Double?
                    var bidPrice: Double?
                    var askPrice: Double?
                    var instrumentEntity: InstrumentEntity?
                    for (curr in data) {
                        if (curr.hasNonNull("symbol")) {
                            symbol = curr.path("symbol").asText()

                            //System.out.println(symbol);

                            instrumentEntity = instrumentEntityRepository!!.findBySymbolName(symbol!!)

                            //System.out.println(instrumentEntity);

                            if (instrumentEntity != null) {

                                /*System.out.println("Будет обновлен: "
                                        + instrumentEntity.getSymbolName());*/

                                if (curr.hasNonNull("highPrice")) {
                                    //highPrice = Float.parseFloat(curr.path("highPrice").asText());
                                    highPrice = curr.path("highPrice").asDouble()
                                    instrumentEntity.highPrice = highPrice
                                }
                                if (curr.hasNonNull("lowPrice")) {
                                    //lowPrice = Float.parseFloat(curr.path("lowPrice").asText());
                                    lowPrice = curr.path("lowPrice").asDouble()
                                    instrumentEntity.lowPrice = lowPrice
                                }
                                if (curr.hasNonNull("lastPrice")) {
                                    //lastPrice = Float.parseFloat(curr.path("lastPrice").asText());
                                    lastPrice = curr.path("lastPrice").asDouble()
                                    instrumentEntity.lastPrice = lastPrice
                                }
                                if (curr.hasNonNull("bidPrice")) {
                                    //bidPrice = Float.parseFloat(curr.path("bidPrice").asText());
                                    bidPrice = curr.path("bidPrice").asDouble()
                                    instrumentEntity.bidPrice = bidPrice
                                }
                                if (curr.hasNonNull("askPrice")) {
                                    //askPrice = Float.parseFloat(curr.path("askPrice").asText());
                                    askPrice = curr.path("askPrice").asDouble()
                                    instrumentEntity.askPrice = askPrice
                                }
                                instrumentEntityRepository!!.save(instrumentEntity)
                            }
                        }
                    }
                }
            }

            if (table == "quoteBin1m" && action == "insert") {
                val data = root.path("data")

                if (data.isArray) {
                    var timestamp: Instant?

                    for (node in data) {
                        timestamp = Instant.parse(node.path("timestamp").asText())
                        quoteBinOneMinuteRepository!!.save(QuoteBinOneMinute(
                                node.path("symbol").asText(),
                                node.path("bidPrice").asDouble(),
                                node.path("askPrice").asDouble(),
                                Date(timestamp!!.toEpochMilli())
                        ))
                    }
                }
            }

            if (table == "quoteBin5m" && action == "insert") {
                val data = root.path("data")

                if (data.isArray) {
                    var timestamp: Instant?

                    for (node in data) {
                        timestamp = Instant.parse(node.path("timestamp").asText())
                        quoteBinFiveMinuteRepository!!.save(QuoteBinFiveMinute(
                                node.path("symbol").asText(),
                                node.path("bidPrice").asDouble(),
                                node.path("askPrice").asDouble(),
                                Date(timestamp!!.toEpochMilli())
                        ))
                    }
                }
            }

        } catch (e: JsonGenerationException) {
            e.printStackTrace()
        } catch (e: JsonMappingException) {
            e.printStackTrace()
        } catch (e: EmptyResultDataAccessException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}
