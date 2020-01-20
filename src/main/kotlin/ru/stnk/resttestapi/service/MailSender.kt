package ru.stnk.resttestapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

import java.util.concurrent.CompletableFuture

@Service
class MailSender {
    @Autowired
    private var mailSender: JavaMailSender? = null

    @Value("\${spring.mail.username}")
    private var username: String = ""

    @Async
    fun send(emailTo: String, subject: String, message: String): CompletableFuture<SimpleMailMessage> {
        val mailMessage = SimpleMailMessage()

        if (username != "") {
            mailMessage.setFrom(username)
        }
        mailMessage.setTo(emailTo)
        mailMessage.setSubject(subject)
        mailMessage.setText(message)

        mailSender!!.send(mailMessage)

        return CompletableFuture.completedFuture(mailMessage)
    }
}