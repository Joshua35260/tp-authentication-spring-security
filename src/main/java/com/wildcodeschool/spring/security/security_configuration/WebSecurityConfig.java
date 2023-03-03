package com.wildcodeschool.spring.security.security_configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.wildcodeschool.spring.security.configuration.WebMvcConfig;
import com.wildcodeschool.spring.security.persistence.enums.RoleEnum;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final String adminRole = RoleEnum.ADMINISTRATOR.name();
	
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);
	public static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Bean
	public PasswordEncoder passwordEncoder() { 
		return passwordEncoder;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Initializing SecurityFilterChain");


		// 4. Configuration des autorisations de route
		http
			.authorizeHttpRequests()
				.requestMatchers("/auth**").authenticated()
				.requestMatchers("/auth/admin**").hasAuthority(adminRole)
				.anyRequest().permitAll()
			.and()
				.exceptionHandling()
				.accessDeniedPage("/errorAccessUnAuthorised")
			.and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/auth")
					.failureHandler((HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) -> {
						System.out.println("error during auth");
						exception.printStackTrace();
						response.sendRedirect("/error?error=" + exception.getMessage());
					})
					.usernameParameter("username")
					.passwordParameter("password")
			.and()
				.logout().invalidateHttpSession(true)
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
			.and()
				.csrf()
			.and()
				.sessionManagement().maximumSessions(1)
				.expiredUrl("/login");

		return http.build();
	}
}
