package ru.stnk.resttestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.stnk.resttestapi.entity.coins.InstrumentEntity

@Repository
interface InstrumentEntityRepository : JpaRepository<InstrumentEntity, Long> {
    // Указанание на то что эта функция может вернуть null важно
    fun findBySymbolName(symbolName: String): InstrumentEntity?
}
