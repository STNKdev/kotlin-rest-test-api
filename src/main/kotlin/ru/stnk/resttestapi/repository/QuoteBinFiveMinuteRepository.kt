package ru.stnk.resttestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.stnk.resttestapi.entity.coins.QuoteBinFiveMinute

@Repository
interface QuoteBinFiveMinuteRepository : JpaRepository<QuoteBinFiveMinute, Long> {
    fun findBySymbolName(symbolName: String): QuoteBinFiveMinute?
    fun findBySymbolNameOrderByTimestampDesc(symbolName: String): List<QuoteBinFiveMinute>?
}
