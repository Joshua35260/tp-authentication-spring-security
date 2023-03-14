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
		logger.info("application started - userRepository=" + userRepository);

		// 2. Initialisation des données

		userRepository.deleteAll();

		var user = new User("user", "user", "My", "user", List.of(RoleEnum.USER));
		userRepository.save(user);

		var admin = new User("admin", "admin", "My", "admin", List.of(RoleEnum.USER, RoleEnum.ADMINISTRATOR));
		userRepository.save(admin);
	}
}
