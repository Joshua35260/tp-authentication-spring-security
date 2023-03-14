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
				.requestMatchers("/auth/admin**").hasRole(RoleEnum.ADMINISTRATOR.name())
				.requestMatchers("/auth**").authenticated()
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

// Voici ce que ce code fait :

// Il définit un logger pour journaliser les événements liés à la sécurité.
// Il crée un objet PasswordEncoder pour encoder les mots de passe des utilisateurs.
// Il définit une SecurityFilterChain pour définir les règles de sécurité pour les requêtes HTTP entrantes.
// Il configure les autorisations de route pour les différentes URL. Par exemple, la route "/auth/admin" est accessible uniquement aux utilisateurs avec le rôle "ADMINISTRATOR". La route "/auth" est accessible uniquement aux utilisateurs authentifiés. Toutes les autres routes sont accessibles à tous les utilisateurs.
// Il gère les erreurs d'accès non autorisées en redirigeant vers une page d'erreur spécifiée.
// Il définit une page de connexion personnalisée ("/login") et configure le traitement de la validation de connexion réussie ou non.
// Il définit une URL de déconnexion ("/logout") et une URL de redirection après la déconnexion réussie ("/login").
// Il active la protection CSRF (Cross-Site Request Forgery) pour empêcher les attaques de type CSRF.
// Il limite le nombre de sessions actives pour chaque utilisateur à un maximum de 1 et définit une URL pour rediriger les sessions expirées.
// En résumé, ce code configure la sécurité de l'application en définissant les règles d'autorisation pour les différentes URL et en définissant les pages de connexion et de déconnexion personnalisées.
