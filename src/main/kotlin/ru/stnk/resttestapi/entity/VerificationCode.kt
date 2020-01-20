package ru.stnk.resttestapi.entity

import java.time.Instant
import javax.persistence.*

// Класс сущности для связки пользователь-проверочный код
@Entity
@Table(name = "users_verification_code")
class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Column(name = "checkCode")
    var checkCode: Int = 9999

    @Column(name = "userEmail")
    var userEmail: String = ""

    @Column(name = "attemps")
    var attemps: Int = 3

    @Column(name = "create_date")
    var createDate: Instant = Instant.now()

    @Column(name = "delay_date")
    var delayDate: Instant? = null

    @Column(name = "expired_date")
    var expiredDate: Instant? = null

    constructor() {}

    constructor(checkCode: Int, userEmail: String, startDelay: Int, expiredDate: Instant) {
        this.checkCode = checkCode
        this.userEmail = userEmail
        this.attemps = 3
        this.createDate = Instant.now()
        this.delayDate = createDate.plusSeconds(startDelay.toLong())
        this.expiredDate = expiredDate
    }
}
