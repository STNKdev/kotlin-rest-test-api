package ru.stnk.resttestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.stnk.resttestapi.entity.VerificationCode
import java.util.*

interface VerificationCodeRepository : JpaRepository<VerificationCode, Long> {
    fun findByUserEmail(email: String): Optional<VerificationCode>
}
