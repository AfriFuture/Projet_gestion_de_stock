## 1. Prérequis

- Java JDK 17 ou plus
- Maven 3.6 ou plus
- MySQL 8 ou plus

La base de données MySQL doit être accessible en local.
Le schéma de base de données utilisé est nommé `gestion_stock`.

## 2. Compilation et exécution du JAR

Pour compiler le projet :

    mvn clean install

Pour exécuter l'application :

    java -jar target/gestionnaireDeStock-0.0.1-SNAPSHOT.jar
## 3. Configuration JDBC

Nom de la base : gestion_stock  
Utilisateur : root  
Mot de passe : root

## 4. Exécution des tests

Pour exécuter les tests JUnit 5 :

    mvn test
