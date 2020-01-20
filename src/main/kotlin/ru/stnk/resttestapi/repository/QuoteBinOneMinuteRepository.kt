package ru.stnk.resttestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.stnk.resttestapi.entity.coins.QuoteBinOneMinute

@Repository
interface QuoteBinOneMinuteRepository : JpaRepository<QuoteBinOneMinute, Long> {
    fun findBySymbolName(symbolName: String): QuoteBinOneMinute?
    fun findBySymbolNameOrderByTimestampDesc(symbolName: String): List<QuoteBinOneMinute>?
}
