# Atelier sur l'authentification avec Spring Boot 3 et Spring Security 6

Dans cet atelier, nous allons voir les bases de l'authentification / autorisation avec Spring Security, en utilisant une base de données que nous allons manier avec Spring Data

## 1. Création de la base de données
Créer la base de données suivante pour l'atelier : 

nom : spring_security_demo
username : springsercurityadmin
password : WCS$@2023

Comme vous pouvez le voir dans [src/main/java/com/wildcodeschool/spring/security/persistence/entities]()

qui contient une seule classe User

Nous aurons une table principale user et une table roles pour mémoriser les rôles de chaque user

Le serveur peut être lancé avec `./mvnw spring-boot:run`, il se relancera automatiquement à chaque modification

## 2. Initialisation des données

Il vous faut maintenant insérer quelques données dans la base

Nous allons faire cela dans la méthode `ExerciseApplication.onStarted` qui est appelée au démarrage

et nous allons utiliser le UserRepository qui est un JpaRepository, que vous avez récemment découvert.

Commencez par injecter dans la classe ExerciseApplication un UserRepository

puis dans onStarted, vider tous les Users, et créez : 
* un `User` qui a un rôle `RoleEnum.USER`
* un `User` qui a deux rôles : `RoleEnum.USER` et `RoleEnum.ADMINISTRATOR`

Démarrez l'application et vérifiez dans votre BDD que les utilisateurs sont bien présents


## 3. Fournir les utilisateurs à Spring Security

Nous avons dans notre application un `UserService` qui permet de récupérer des utilisateurs

Pour que Spring Security puisse récupérer les utilisateurs, il faut qu'il implémente `org.springframework.security.core.userdetails.UserDetailsService`

Attention, il va falloir implémenter la méthode demander par cette interface

Relancer `./mvnw spring-boot:run` pour vérifier que tout compile et démarre correctement

## 4. Configuration des autorisations de route

Prenez connaissance de toute la filterChain qui a été configurée (dans la classe annotée `EnableWebSecurity`), avec une page de login, une page d'erreur, un gestionnaire d'erreur d'authentification.

Pour l'instant, toutes les pages du site sont accessibles.

Nous voulons que toutes les pages correspondant à ce pattern : `/auth**` ne soient accessibles qu'aux utilisateurs connectés.

Activer ce filtre, relancer le serveur et vérifiez que vous ne pouvez pas accéder à `http://localhost:8080/auth` sans être connecté avec l'un de vos users

Ensuite, nous voulons que seul les utilisateurs ayant un role `ADMINISTRATOR` puisse accéder aux pages de type `/auth/admin**`

Activer ce filtre, relancer le serveur et vérifiez que vous ne pouvez accéder à `http://localhost:8080/auth/admin` sans être connecté avec l'administrateur

## 5. Nouveau module pour le site !

Rajouter une nouvelle page pour ce site, accessible seulement à un rôle `RoleEnum.SUPER_ADMIN`

Elle doit être accessible à l'adresse suivante : 
[http://localhost:8080/auth/superadmin]()

Attention, il faut aussi déclarer la vue WebMvcConfig
