package com.wildcodeschool.spring.security.persistence.services;

import com.wildcodeschool.spring.security.persistence.entities.User;
import com.wildcodeschool.spring.security.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// 3. Fournir les utilisateurs à Spring Security
	public UserDetails getUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("No user present with username : " + username);
		} else {
			return user;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return getUserByUsername(username);
	}
}

// Ce service est une implémentation de l'interface UserDetailsService de Spring Security. Il permet de récupérer les informations d'un utilisateur à partir de son nom d'utilisateur (username) et de les fournir à Spring Security pour qu'il puisse authentifier l'utilisateur.

// La méthode getUserByUsername est utilisée pour récupérer l'utilisateur à partir de son nom d'utilisateur. Si l'utilisateur n'est pas trouvé, une exception UsernameNotFoundException est levée.

// La méthode loadUserByUsername est la méthode principale de UserDetailsService. Elle appelle simplement la méthode getUserByUsername pour récupérer l'utilisateur et la renvoie à Spring Security.

// Ce service est nécessaire pour que Spring Security puisse récupérer les informations de l'utilisateur pour l'authentification et l'autorisation. Sans cette implémentation, Spring Security ne saurait pas comment récupérer les informations d'un utilisateur à partir de son nom d'utilisateur.