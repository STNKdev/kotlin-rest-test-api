package ru.stnk.RestTestAPI;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import ru.stnk.RestTestAPI.entity.Roles;
import ru.stnk.RestTestAPI.entity.User;
import ru.stnk.RestTestAPI.repository.RolesRepository;
import ru.stnk.RestTestAPI.repository.UserRepository;
import ru.stnk.RestTestAPI.service.SimpleWsHandler;

import java.util.ArrayList;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class RestTestAPIApplication {

	@Bean
	CommandLineRunner init (RolesRepository rolesRepository, UserRepository userRepository) {

		return (evt) -> {
			if (rolesRepository.findAll().isEmpty()) {
				rolesRepository.save(new Roles("ROLE_ADMIN"));
				rolesRepository.save(new Roles("ROLE_USER"));
			}

			if (!userRepository.findByEmail("admin@test.io").isPresent()) {
				User admin = new User();
				admin.setEmail("admin@test.io");
				admin.setPassword("123");
				admin.setPhone("88002000600");
				admin.setOs("web");
				admin.setEnabled(true);
				admin.setEmailConfirmed(true);
				admin.setFreeBalance((long) 999999);
				admin.setRoles(new ArrayList<>());
				admin.addRole(rolesRepository.findByName("ROLE_ADMIN"));
				admin.addRole(rolesRepository.findByName("ROLE_USER"));
				admin.setBetBalance((long) 0);
				admin.setWithdrawalBalance((long) 0);

				userRepository.save(admin);
			}
		};
	}

	@Bean
	public WebSocketConnectionManager wsConnectionManager(SimpleWsHandler simpleWsHandler) {

		final String webSocketUri = "wss://www.bitmex.com/realtime?" +
				"subscribe=instrument:XBTUSD,instrument:ETHUSD,instrument:LTCU19,instrument:XRPU19," +
				"quoteBin1m:XBTUSD,quoteBin1m:ETHUSD,quoteBin1m:LTCU19,quoteBin1m:XRPU19,"+
				"quoteBin5m:XBTUSD,quoteBin5m:ETHUSD,quoteBin5m:LTCU19,quoteBin5m:XRPU19";

		//Создает web socket подключение
		WebSocketConnectionManager manager = new WebSocketConnectionManager(
				new StandardWebSocketClient(),
				simpleWsHandler, //Должен быть определен для обработки сообщений
				webSocketUri);


		manager.setAutoStartup(true);

		return manager;
	}

	public static void main(String[] args) {
		SpringApplication.run(RestTestAPIApplication.class, args);
	}

}
