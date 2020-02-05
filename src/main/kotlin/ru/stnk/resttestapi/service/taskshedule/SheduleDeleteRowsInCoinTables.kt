package ru.stnk.resttestapi.service.taskshedule

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.stnk.resttestapi.repository.QuoteBinFiveMinuteRepository
import ru.stnk.resttestapi.repository.QuoteBinOneMinuteRepository


@Component
@EnableScheduling
class SheduleDeleteRowsInCoinTables (
        @Autowired val quoteBinOneMinuteRepository: QuoteBinOneMinuteRepository,
        @Autowired val quoteBinFiveMinuteRepository: QuoteBinFiveMinuteRepository
) {
    /*@Autowired
    private val quoteBinOneMinuteRepository: QuoteBinOneMinuteRepository? = null

    @Autowired
    private val quoteBinFiveMinuteRepository: QuoteBinFiveMinuteRepository? = null*/

    val logger: Logger = LoggerFactory.getLogger(SheduleDeleteRowsInCoinTables::class.java)

    @Scheduled(fixedDelay = 60000)
    fun deleteRows() {
        val oneMinuteList = quoteBinOneMinuteRepository.findAll()
        val fiveMinuteList = quoteBinFiveMinuteRepository.findAll()

        if (oneMinuteList.size > 500) {

            logger.debug("Усекаю таблицу quote_bin_one_minute")

            for (i in 0 until oneMinuteList.size - 499) {
                try {
                    quoteBinOneMinuteRepository.delete(oneMinuteList[i])
                    logger.debug("Удалил строку $i")
                } catch (e: Exception) {
                    logger.debug("Ошибка при удалении в таблице: " + e)
                }

            }
        }

        if (fiveMinuteList.size > 500) {

            logger.debug("Усекаю таблицу quote_bin_five_minute")

            for (i in 0 until fiveMinuteList.size - 499) {
                try {
                    quoteBinFiveMinuteRepository.delete(fiveMinuteList[i])
                    logger.debug("Удалил строку $i")
                } catch (e: Exception) {
                    logger.debug("Ошибка при удалении в таблице: " + e)
                }
            }
        }
    }
}
