package com.wildcodeschool.spring.security.persistence.entities;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wildcodeschool.spring.security.persistence.enums.RoleEnum;
import com.wildcodeschool.spring.security.security_configuration.WebSecurityConfig;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user")
    private Long idUser;

    @NotNull
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @ElementCollection(targetClass = RoleEnum.class, fetch = FetchType.EAGER)
    @Cascade(value = CascadeType.REMOVE)
    @JoinTable(
            indexes = {@Index(name = "INDEX_USER_ROLE", columnList = "id_user")},
            name = "roles",
            joinColumns = @JoinColumn(name = "id_user")
    )
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Collection<RoleEnum> roles;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    @Column(name = "enabled")
    private boolean enabled;

    public User() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.roles = Collections.singletonList(RoleEnum.USER);
    }

    public User(String username, String password, String firstname, String lastname, Collection<RoleEnum> roles) {
        this.username = username;
        this.password = WebSecurityConfig.passwordEncoder.encode(password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.roles = roles;
    }

    
    
    public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Collection<RoleEnum> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleEnum> roles) {
		this.roles = roles;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // String roles = StringUtils.collectionToCommaDelimitedString(getRoles().stream()
        //         .map(Enum::name).collect(Collectors.toList()));
        // return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);

		List<SimpleGrantedAuthority> userAuthorities = new ArrayList<>();
		for (RoleEnum role : roles) {
			userAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
			// ===> ROLE_ADMINISTRATOR
		}
		return userAuthorities;
    }

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}

// Ce code est une définition de la classe "User" qui représente un utilisateur de l'application. Cette classe implémente l'interface UserDetails qui est utilisée par Spring Security pour gérer les utilisateurs et les autorisations d'accès.

// La classe User a plusieurs propriétés qui correspondent aux informations de l'utilisateur, telles que le nom d'utilisateur, le mot de passe, le prénom, le nom de famille, les rôles de l'utilisateur, et d'autres propriétés liées à la sécurité comme la validité de son compte, la validité de ses identifiants, et si son compte est activé.

// Les rôles sont stockés dans une collection de type RoleEnum, qui est une énumération qui définit les différents rôles possibles pour un utilisateur dans l'application.

// La méthode getAuthorities() de l'interface UserDetails renvoie une collection de GrantedAuthority, qui est utilisée par Spring Security pour vérifier si un utilisateur a accès à une ressource particulière. Dans cette méthode, les rôles de l'utilisateur sont convertis en SimpleGrantedAuthority (qui implémente l'interface GrantedAuthority) pour être utilisés par Spring Security.

// La classe User utilise également des annotations de persistance de Jakarta Persistence (anciennement Java Persistence API ou JPA) pour définir les relations entre les tables de la base de données et les propriétés de l'entité. Ces annotations sont utilisées par le système de persistance pour générer les requêtes SQL nécessaires à la gestion de l'entité User en base de données.

// Enfin, la classe User contient un constructeur sans paramètre et un constructeur avec des paramètres pour faciliter la création d'un nouvel utilisateur.

// serialVersionUID est un identifiant unique de version pour une classe sérialisable. Lorsqu'une classe implémente l'interface Serializable, elle doit fournir un identifiant de version unique pour assurer que les objets de cette classe puissent être désérialisés correctement même si la classe subit des modifications entre les moments de la sérialisation et de la désérialisation.

// En d'autres termes, si un objet est sérialisé avec une certaine version d'une classe et que la classe est modifiée par la suite, la désérialisation de l'objet peut échouer car la version de la classe a changé. Pour éviter cela, il est recommandé de définir explicitement un identifiant de version unique pour chaque classe sérialisable.

// Dans ce cas précis, private static final long serialVersionUID = 1L; définit l'identifiant de version pour la classe User. Le numéro de version est défini à 1L, ce qui signifie que c'est la première version de la classe. Si la classe User est modifiée à l'avenir, cet identifiant devrait être modifié en conséquence pour refléter la nouvelle version de la classe.

// Ce code définit une relation entre deux entités de la base de données : l'entité User et l'entité RoleEnum. Cette relation est représentée par une table de jointure "roles", qui contient les rôles associés à chaque utilisateur.

// @ElementCollection: indique que cette relation est une collection d'éléments (les rôles). Cette annotation est utilisée pour les relations OneToMany où l'autre côté est une collection, comme c'est le cas ici.

// targetClass = RoleEnum.class: indique que les éléments de la collection sont des instances de la classe RoleEnum.

// fetch = FetchType.EAGER: indique que les rôles doivent être chargés en même temps que l'utilisateur. Cela permet d'éviter les problèmes de performance liés au chargement paresseux (lazy loading) des collections.

// @Cascade(value = CascadeType.REMOVE): indique que lorsque l'utilisateur est supprimé, tous ses rôles doivent également être supprimés. C'est ce qu'on appelle une cascade de suppression.

// @JoinTable: spécifie le nom de la table de jointure et les colonnes qui la lient à la table User.

// @Index(name = "INDEX_USER_ROLE", columnList = "id_user"): crée un index sur la colonne id_user pour améliorer les performances de recherche.

// @JoinColumn(name = "id_user"): indique que la colonne id_user de la table roles fait référence à la clé primaire de la table User.

// @Column(name = "role", nullable = false): spécifie le nom de la colonne qui contiendra les rôles, ainsi que le fait qu'elle ne peut pas être nulle.

// @Enumerated(EnumType.STRING): indique que les valeurs stockées dans la colonne "role" sont des chaînes de caractères qui représentent les noms des valeurs de l'énumération RoleEnum.

// Il existe plusieurs options pour la méthode Cascade :

// CascadeType.ALL : Propage toutes les opérations de persistance (PERSIST, MERGE, REMOVE, REFRESH, DETACH).
// CascadeType.PERSIST : Propage l'opération de persistance (PERSIST).
// CascadeType.MERGE : Propage l'opération de fusion (MERGE).
// CascadeType.REMOVE : Propage l'opération de suppression (REMOVE).
// CascadeType.REFRESH : Propage l'opération de rafraîchissement (REFRESH).
// CascadeType.DETACH : Propage l'opération de détachement (DETACH).
// Dans ce code, CascadeType.REMOVE a été utilisé pour assurer que tous les rôles associés à un utilisateur sont supprimés lorsque l'utilisateur est supprimé. Cela peut être utile dans les cas où il est important de maintenir une cohérence des données, ou lorsque la suppression d'une entité entraîne automatiquement la suppression d'autres entités associées.