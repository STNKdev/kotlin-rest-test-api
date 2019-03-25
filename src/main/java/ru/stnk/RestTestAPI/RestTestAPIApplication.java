package ru.stnk.RestTestAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
@EnableJpaAuditing
public class RestTestAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestTestAPIApplication.class, args);
	}

}
