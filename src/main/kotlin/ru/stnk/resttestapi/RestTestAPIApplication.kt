package ru.stnk.resttestapi

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.socket.client.WebSocketConnectionManager
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import ru.stnk.resttestapi.entity.Roles
import ru.stnk.resttestapi.entity.User
import ru.stnk.resttestapi.repository.RolesRepository
import ru.stnk.resttestapi.repository.UserRepository
import ru.stnk.resttestapi.service.SimpleWsHandler

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
class RestTestAPIApplication {

    private val logger: Logger = LoggerFactory.getLogger(RestTestAPIApplication::class.java)

    @Bean
    fun init(rolesRepository: RolesRepository, userRepository: UserRepository): CommandLineRunner {
        return CommandLineRunner {
            if (rolesRepository.findAll().isEmpty()) {
                logger.info("Добавление роли Админа")
                rolesRepository.save(Roles("ROLE_ADMIN"))
                logger.info("Добавление роли Пользователя")
                rolesRepository.save(Roles("ROLE_USER"))
            }
            if (!userRepository.findByEmail("admin@test.io").isPresent) {
                logger.info("Добавление Админа")
                val admin = User()
                admin.email = "admin@test.io"
                admin.password = "123"
                admin.phone = "88002000600"
                admin.os = "web"
                admin.isEnabled = true
                admin.emailConfirmed = true
                admin.freeBalance = 999999
                // Потому что свойство roles проинициализированно по умолчанию как ArrayList()
                //admin.roles = ArrayList()
                admin.roles.add(rolesRepository.findByName("ROLE_ADMIN"))
                admin.roles.add(rolesRepository.findByName("ROLE_USER"))
                admin.betBalance = 0
                admin.withdrawalBalance = 0
                userRepository.save(admin)
            }
        }
    }

    @Bean
    fun wsConnectionManager(simpleWsHandler: SimpleWsHandler): WebSocketConnectionManager {
        val webSocketUri = "wss://www.bitmex.com/realtime?" +
                "subscribe=instrument:XBTUSD,instrument:ETHUSD," + // "instrument:LTCU19,instrument:XRPU19," +
                "quoteBin1m:XBTUSD,quoteBin1m:ETHUSD," + // "quoteBin1m:LTCU19,quoteBin1m:XRPU19," +
                "quoteBin5m:XBTUSD,quoteBin5m:ETHUSD" // "quoteBin5m:LTCU19,quoteBin5m:XRPU19"
        //Создает web socket подключение
        val manager = WebSocketConnectionManager(
                StandardWebSocketClient(),
                simpleWsHandler,  //Должен быть определен для обработки сообщений
                webSocketUri)
        manager.isAutoStartup = true
        return manager
    }

    /*companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            //runApplication<RestTestAPIApplication>(*args)
            SpringApplication.run(RestTestAPIApplication::class.java, *args)
        }
    }*/
}

fun main(args: Array<String>) {
    SpringApplication.run(RestTestAPIApplication::class.java, *args)
}