package com.wildcodeschool.spring.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import com.wildcodeschool.spring.security.persistence.entities.User;
import com.wildcodeschool.spring.security.persistence.enums.RoleEnum;
import com.wildcodeschool.spring.security.persistence.repositories.UserRepository;

@SpringBootApplication
public class ExerciseApplication {
	private static Logger logger = LoggerFactory.getLogger(ExerciseApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ExerciseApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;

	@EventListener
	public void onStarted(ApplicationStartedEvent event) {
		logger.info("application started");

		// 2. Initialisation des données

		logger.info("Initializing users");
		userRepository.deleteAll();

		// Ceci n'est pas à faire en production
		List<RoleEnum> userRole = List.of(RoleEnum.USER);
		List<RoleEnum> adminRole = List.of(RoleEnum.USER, RoleEnum.ADMINISTRATOR);
		User user = new User("user", "user", "User", "USER", userRole);
		User adminUser = new User("admin", "admin", "Admin", "ADMIN", adminRole);
		userRepository.save(user);
		userRepository.save(adminUser);
	}
}
