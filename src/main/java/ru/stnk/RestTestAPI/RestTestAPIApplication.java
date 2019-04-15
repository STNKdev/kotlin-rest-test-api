package ru.stnk.RestTestAPI;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import org.springframework.scheduling.annotation.EnableAsync;
import ru.stnk.RestTestAPI.entity.Roles;
import ru.stnk.RestTestAPI.entity.User;
import ru.stnk.RestTestAPI.repository.RolesRepository;
import ru.stnk.RestTestAPI.repository.UserRepository;

import java.util.ArrayList;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
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
				admin.setEnableUser(true);
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

	public static void main(String[] args) {
		SpringApplication.run(RestTestAPIApplication.class, args);
	}

}
