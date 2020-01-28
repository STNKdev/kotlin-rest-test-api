package ru.stnk.resttestapi.service.taskshedule

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.socket.client.WebSocketConnectionManager

@Component
@EnableScheduling
class SheduleCheckWebSocketConnection (
        @Autowired val wsConnectionManager: WebSocketConnectionManager
) {

    val logger = LoggerFactory.getLogger(SheduleCheckWebSocketConnection::class.java)

    @Scheduled(fixedDelay = 60000)
    fun checkConnection () {
        logger.debug("Проверка статуса WebSocket соединения с BitMex")
        if(!wsConnectionManager.isRunning) {
            logger.debug("Попытка перезапустить WebSocket соединение с BitMex")
            wsConnectionManager.stop()
            wsConnectionManager.start()
            if (wsConnectionManager.isRunning) {
                logger.debug("WebSocket соединение с BitMex запущено")
            }
        } else {
            logger.debug("WebSocket соединение с BitMex активно")
        }
    }
}