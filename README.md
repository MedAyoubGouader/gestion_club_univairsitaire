# 🎓 Système de Gestion de Club Universitaire (G-CLUB)

Bienvenue dans le projet **G-CLUB**, une application de bureau conçue en **Java (JavaFX)** et connectée à une base de données **Oracle 11g**. Ce projet permet de gérer facilement les membres d'un club universitaire, l'organisation d'événements, ainsi que le suivi des présences.

## ✨ Fonctionnalités Principales

1. **Gestion des Membres** :
   - Ajout, modification, et suppression de membres.
   - Validation stricte des adresses e-mail (Regex).
   - Empêchement des doublons (Nom + Prénom uniques).
   - Rôles définis via une liste déroulante.
   - Modélisation du statut : Un membre est par défaut **Inactif**. S'il participe à un événement, son statut passe à **Actif**.

2. **Gestion des Événements** :
   - Création, modification et suppression d'événements (Titre, Date, Lieu, Description).
   - Interface intuitive interagissant avec la base de données.

3. **Gestion des Participations (Association)** :
   - **Sous-interface** déclenchée par un double-clic ou via un bouton dédié.
   - Recherche intelligente d'un membre par son **ID** ou son **Nom complet**.
   - **Transaction SQL Automatisée** : L'ajout modifie instantanément le statut global du membre.

---

## 📂 Architecture Détaillée et Explication de CHAQUE Fichier

Le projet suit le motif de conception (Design Pattern) **MVC simplifié / DAO** permettant de séparer l'interface graphique (View), la logique métier (Model), et l'accès aux données (DAO/Database).

### 1. Le Point d'Entrée (Racine du package `application.club`)

* **`Lancement.java`**
  * **Rôle** : C'est le vrai point de lancement de l'application (`public static void main`).
  * **Détail technique** : Il ne fait qu'une seule chose : appeler `ApplicationPrincipale.main()`. Pourquoi faire cela ? Depuis Java 11, JavaFX exige que les classes héritant de `Application` soient déclarées dans des "modules". Créer ce `Lancement.java` séparé qui n'hérite pas d'Application est une astuce technique très courante pour contourner ce blocage et lancer le projet sans configurer de fichier complexe `module-info.java`.

* **`ApplicationPrincipale.java`**
  * **Rôle** : C'est le cœur de l'interface utilisateur. C'est ici que la fenêtre globale se dessine.
  * **Détail technique** : Il hérite de `javafx.application.Application`. Il construit le conteneur principal (un `BorderPane`). Il crée le menu latéral gauche (Sidebar) avec une `VBox` stylisée (couleur `#2c3e50`) et les boutons de navigation "Membres" et "Événements".
  * **Logique de rafraîchissement** : C'est ici que les actions des clics sur les boutons du menu sont gérées. Chaque fois que l'utilisateur clique sur "Gestion Membres", `ApplicationPrincipale.java` appelle `FenetreMembre.loadData()` pour forcer l'interrogation de la base de données à nouveau avant d'afficher la page. Cela garantit que la vue est toujours synchronisée avec Oracle, notamment quand un statut passe à "Actif".

### 2. Le Modèle (`application.club.donnees`)
Ces classes sont des "POJO" (Plain Old Java Objects). Elles ne contiennent pas de logique complexe (pas de requêtes SQL ni d'interface). Elles servent juste à stocker les données en mémoire de manière organisée.

* **`Membre.java`**
  * **Rôle** : Représente conceptuellement une ligne de la table `Membre` d'Oracle.
  * **Détail technique** : Contient les attributs `idMembre`, `nom`, `prenom`, `email`, `telephone`, `poste`, `nbEvenements` et `statutActif`. Il inclut plusieurs constructeurs (avec ou sans ID) pour faciliter la création des objets, et tous les `Getters` et `Setters` nécessaires pour que JavaFX puisse lire ces informations et les afficher dans le tableau (TableView).

* **`Evenement.java`**
  * **Rôle** : Représente conceptuellement une ligne de la table `Evenement`.
  * **Détail technique** : Gère l'ID, le Titre, la Date (utilise `java.sql.Date` pour être 100% structuré comme un DATE Oracle), le Lieu et la Description de la même manière que Membre.

### 3. L'Accès aux Données - DAO (`application.club.basededonnees`)
Les "Data Access Objects" (Objet d'Accès aux Données) concentrent absolument toutes les requêtes SQL via l'API JDBC. Aucune requête SQL ne doit exister en dehors de ce dossier.

* **`RequetesMembre.java`**
  * **Rôle** : Gérer les interactions directes avec la base de données pour les membres.
  * **Méthodes clés** :
    * `ajouterMembre()`, `modifierMembre()`, `supprimerMembre()` : Exécutent des requêtes `PreparedStatement` (les `?`) pour éviter toute attaque par injection SQL. Par défaut, la requête d'ajout insère un membre avec le statut statique "Inactif", comme le demande le cahier des charges.
    * `obtenirTousLesMembres()` : Execute un `SELECT` complexe qui inclut la colonne physique `statut` ET une sous-requête comptant le nombre d'événements (dans la table d'association) auxquels chaque membre a participé pour l'afficher dans les statistiques.
    * `existeMembre()` : Execute un `COUNT(*)` pour vérifier si un duo Nom/Prénom existe déjà afin d'éviter les doublons stricts.
    * `rechercherMembreIdOuNom()` : Construit dynamiquement une requête SQL `LIKE` ou `=` selon si l'utilisateur saisit un texte ou un chiffre explicite dans la barre de recherche intelligente de la popup d'événements.

* **`RequetesEvenement.java`**
  * **Rôle** : Similaire à RequetesMembre, mais pour les événements (Insert, Update, Delete, Select All pour charger le tableau central).

* **`RequetesParticipation.java`**
  * **Rôle** : Gère tout ce qui touche à la table d'association `ParticipationEvenement`. C'est le fichier métier le plus avancé du projet.
  * **Méthode clé `ajouterParticipation()`** : Utilise le concept de **Transaction SQL**. Elle désactive l'autocommit (`conn.setAutoCommit(false)`). Ensuite, elle envoie 1) un `INSERT INTO ParticipationEvenement` et 2) un `UPDATE Membre SET statut='Actif'`. Si l'une des deux opérations échoue (par exemple une déconnexion), elle déclenche un `rollback()` pour annuler les deux. C'est ce qui garantit la stricte cohérence du statut vis-à-vis des présences.
  * **Méthode `obtenirParticipants()`** : Effectue une jointure SQL interne (`JOIN`) entre Membre et ParticipationEvenement pour lister dans la fenêtre popup quels membres exacts assistent à l'événement sélectionné.

### 4. L'Interface Graphique (`application.club.interfaces`)
Les "Vues" gèrent l'affichage des composants JavaFX, collectent les saisies de l'utilisateur et appellent les fonctions des DAO correspondantes en réponse.

* **`FenetreMembre.java`**
  * **Rôle** : L'écran complet construit (partie droite) quand on navigue sur "Gestion des Membres".
  * **Détail technique** :
    * Construit le `TableView` et associe ses colonnes aux attributs de la classe `Membre` (via des astuces mémoires nommées `PropertyValueFactory`).
    * Construit le formulaire (TextFields, et une `ComboBox` pour que le "Poste" soit choisis dans une liste fermée et contrôlée).
    * Méthode `validateEmail()` : Contient une expression régulière (Regex) `^[A-Za-z0-9+_.-]+@(.+\\..+)$` interdisant à l'utilisateur d'ajouter un membre si l'email ne contient pas `@` et un domaine (`.com`). Affiche une alerte bloquante le cas échéant.
    * Gère le `addListener` du tableau : le clic sur une ligne remplit automatiquement tous les `TextField` du bas pour rendre la modification et la suppression hyper-rapides.

* **`FenetreEvenement.java`**
  * **Rôle** : L'écran complet visible quand on gère les événements et sa sous-interface pop-up de présences.
  * **Détail technique** :
    * Construit via VBox et GridPane le tableau et le formulaire des événements (incluant un sélecteur `DatePicker`).
    * **L'événement Double-Clic** : La méthode `setRowFactory` capture les clics physiques de la souris. Si `getClickCount() == 2`, alors la vue déclenche l'ouverture de la fenêtre des présences.
    * **La Sous-interface** : `ouvrirFenetrePresences()` crée matériellement un nouveau `Stage` (une fenêtre pop-up) configuré en `Modality.APPLICATION_MODAL`. (L'interface derrière devient grisée et intouchable). Cette pop-up contient son mini-tableau de présence, sa barre de recherche textuelle liée à `rechercherMembreIdOuNom()` et exécute le code métier SQL de `RequetesParticipation` au clic sur le bouton Ajouter.

### 5. Config et Base de Données (`application.club.outils` et Ressources)

* **`ConnexionOracle.java`**
  * **Rôle** : Centraliser la connexion technique au serveur Oracle.
  * **Design Pattern (Singleton)** : La méthode `getConnection()` vérifie si la connexion (`Connection`) est déjà instanciée ou fermée. Si elle n'existe pas, elle ouvre une connexion à `jdbc:oracle:thin:@localhost:1521:xe` (OJDBC8). Sinon, elle renvoie la connexion préalablement ouverte, économisant énormément de mémoire et de temps de traitement.
  * **Méthode `mettreAjourBaseDeDonnees()`** : C'est un mini-outil de migration appelé à chaque lancement de l'application. Elle force un `ALTER TABLE Membre ADD statut VARCHAR2(50) DEFAULT 'Inactif'` pour s'assurer que la base compte bien cette colonne requise pour les événements. Si la colonne existe (code Oracle `ORA-01430`), l'erreur est volontairement ignorée pour ne pas perturber le lancement.

* **`src/main/resources/database/schema.sql`** (et `setup_db.sql` optionnel)
  * **Rôle** : Définition brute SQL de la structure complète d'implémentation.
  * **Détail technique** : Code les requêtes `CREATE TABLE` des 3 tables (`Membre`, `Evenement`, `ParticipationEvenement`). Sous Oracle 11g, l'incrémentation automatique MySQL (`AUTO_INCREMENT`) n'existe pas. Ces scripts intègrent donc impérativement des `CREATE SEQUENCE` couplés à des `CREATE OR REPLACE TRIGGER` qui injectent dynamiquement l'ID calculé (ex: `SEQ_MEMBRE.NEXTVAL`) juste avant chaque validation d'un nouveau membre.

* **`pom.xml`**
  * **Rôle** : Configuration de Maven (Le gestionnaire de dépendances et de build du projet).
  * **Détail technique** :
    * Déclare la version de compilation du compilateur (Java 17).
    * Télécharge et relie automatiquement les librairies `javafx-controls` et `javafx-fxml` pour pouvoir dessiner les fenêtres sans Scene Builder.
    * Spécifie la dépendance au driver propriétaire de base de données `ojdbc8` d'Oracle (version 19) pour que la librairie `java.sql` puisse transcoder les requêtes vers le serveur 11g XE installé sur la machine.

---

## 🚀 Comment lancer le projet ?

1. **Base de données Oracle** : Assurez-vous d'avoir Oracle 11g d'installé en local. Connectez-vous avec votre outil préféré (SQLPlus, SQL Developer) en `sysdba`, puis exécutez le script SQL pour créer l'utilisateur `gestionclub` (mot de passe: `gestion123`). Lancez ensuite le code de création (`schema.sql`) pour bâtir vos tables et vos séquences.
2. **IDE (IntelliJ IDEA)** : Ouvrez le projet à sa racine en tant que projet **Maven** (Ouvrir le pom.xml). Attendez de préférence quelques secondes que Maven finisse de télécharger les dépendances (Indexation).
3. **Exécution Windows/Mac** : Assurez-vous d'avoir un SDK Java 17 installé. Ouvrez purement et simplement le fichier `src/main/java/application/club/Lancement.java`, faites clic-droit et sélectionnez "Run 'Lancement.main()'".
# gestion_club_univairsitaire
