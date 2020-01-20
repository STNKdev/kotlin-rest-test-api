package ru.stnk.resttestapi.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.stnk.resttestapi.repository.QuoteBinFiveMinuteRepository
import ru.stnk.resttestapi.repository.QuoteBinOneMinuteRepository

@Component
class SheduleDeleteRowsInCoinTables {
    @Autowired
    private val quoteBinOneMinuteRepository: QuoteBinOneMinuteRepository? = null

    @Autowired
    private val quoteBinFiveMinuteRepository: QuoteBinFiveMinuteRepository? = null

    @Scheduled(fixedDelay = 60000)
    fun deleteRows() {
        val oneMinuteList = quoteBinOneMinuteRepository!!.findAll()
        val fiveMinuteList = quoteBinFiveMinuteRepository!!.findAll()

        if (oneMinuteList.size > 500) {
            for (i in 0 until oneMinuteList.size - 499) {
                quoteBinOneMinuteRepository.delete(oneMinuteList[i])
            }
        }

        if (fiveMinuteList.size > 500) {
            for (i in 0 until fiveMinuteList.size - 499) {
                quoteBinFiveMinuteRepository.delete(fiveMinuteList[i])
            }
        }
    }
}
