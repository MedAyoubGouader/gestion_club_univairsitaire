-- Script de création de la base de données pour la gestion d'un club universitaire (ORACLE 11g)

-- 1. Administrateur
CREATE TABLE Administrateur (
    idAdmin NUMBER PRIMARY KEY,
    nom VARCHAR2(50) NOT NULL,
    prenom VARCHAR2(50) NOT NULL,
    login VARCHAR2(50) UNIQUE NOT NULL,
    motDePasse VARCHAR2(255) NOT NULL
);

CREATE SEQUENCE seq_admin START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_admin 
BEFORE INSERT ON Administrateur 
FOR EACH ROW 
BEGIN
  IF :new.idAdmin IS NULL THEN
    SELECT seq_admin.NEXTVAL INTO :new.idAdmin FROM dual;
  END IF;
END;
/

-- 2. Membre
CREATE TABLE Membre (
    idMembre NUMBER PRIMARY KEY,
    nom VARCHAR2(50) NOT NULL,
    prenom VARCHAR2(50) NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    telephone VARCHAR2(20),
    poste VARCHAR2(50),
    statut VARCHAR2(50) DEFAULT 'Inactif'
);

CREATE SEQUENCE seq_membre START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER trg_membre 
BEFORE INSERT ON Membre 
FOR EACH ROW 
BEGIN
  IF :new.idMembre IS NULL THEN
    SELECT seq_membre.NEXTVAL INTO :new.idMembre FROM dual;
  END IF;
END;
/

-- 3. Evenement
CREATE TABLE Evenement (
    idEvenement NUMBER PRIMARY KEY,
    titre VARCHAR2(100) NOT NULL,
    dateEvenement DATE NOT NULL,
    lieu VARCHAR2(100),
    description CLOB
);

CREATE SEQUENCE seq_evenement START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER trg_evenement 
BEFORE INSERT ON Evenement 
FOR EACH ROW 
BEGIN
  IF :new.idEvenement IS NULL THEN
    SELECT seq_evenement.NEXTVAL INTO :new.idEvenement FROM dual;
  END IF;
END;
/

-- 4. Reunion
CREATE TABLE Reunion (
    idReunion NUMBER PRIMARY KEY,
    dateReunion DATE NOT NULL,
    -- Pas de type TIME en Oracle, on gère les heures via des DATE ou VARCHAR
    heureReunion VARCHAR2(10) NOT NULL, 
    sujet VARCHAR2(255) NOT NULL,
    duree NUMBER -- Duree en minutes
);

CREATE SEQUENCE seq_reunion START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER trg_reunion 
BEFORE INSERT ON Reunion 
FOR EACH ROW 
BEGIN
  IF :new.idReunion IS NULL THEN
    SELECT seq_reunion.NEXTVAL INTO :new.idReunion FROM dual;
  END IF;
END;
/


-- 5. Tache
CREATE TABLE Tache (
    idTache NUMBER PRIMARY KEY,
    description VARCHAR2(255) NOT NULL,
    etat VARCHAR2(50) DEFAULT 'En cours',
    dateLimite DATE,
    idMembre NUMBER,
    CONSTRAINT fk_tache_membre FOREIGN KEY (idMembre) REFERENCES Membre(idMembre) ON DELETE SET NULL
);

CREATE SEQUENCE seq_tache START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER trg_tache 
BEFORE INSERT ON Tache 
FOR EACH ROW 
BEGIN
  IF :new.idTache IS NULL THEN
    SELECT seq_tache.NEXTVAL INTO :new.idTache FROM dual;
  END IF;
END;
/


-- 6. Cotisation
CREATE TABLE Cotisation (
    idCotisation NUMBER PRIMARY KEY,
    montant NUMBER(10,2) NOT NULL,
    datePaiement DATE NOT NULL,
    idMembre NUMBER,
    CONSTRAINT fk_cotisation_membre FOREIGN KEY (idMembre) REFERENCES Membre(idMembre) ON DELETE CASCADE
);

CREATE SEQUENCE seq_cotisation START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER trg_cotisation 
BEFORE INSERT ON Cotisation 
FOR EACH ROW 
BEGIN
  IF :new.idCotisation IS NULL THEN
    SELECT seq_cotisation.NEXTVAL INTO :new.idCotisation FROM dual;
  END IF;
END;
/

-- 7. Depense
CREATE TABLE Depense (
    idDepense NUMBER PRIMARY KEY,
    montant NUMBER(10,2) NOT NULL,
    dateDepense DATE NOT NULL,
    motif VARCHAR2(255) NOT NULL,
    idAdmin NUMBER,
    CONSTRAINT fk_depense_admin FOREIGN KEY (idAdmin) REFERENCES Administrateur(idAdmin) ON DELETE SET NULL
);

CREATE SEQUENCE seq_depense START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER trg_depense 
BEFORE INSERT ON Depense 
FOR EACH ROW 
BEGIN
  IF :new.idDepense IS NULL THEN
    SELECT seq_depense.NEXTVAL INTO :new.idDepense FROM dual;
  END IF;
END;
/

-- 8. Materiel
CREATE TABLE Materiel (
    idMateriel NUMBER PRIMARY KEY,
    nom VARCHAR2(100) NOT NULL,
    quantite NUMBER DEFAULT 0 NOT NULL,
    etat VARCHAR2(50)
);

CREATE SEQUENCE seq_materiel START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER trg_materiel 
BEFORE INSERT ON Materiel 
FOR EACH ROW 
BEGIN
  IF :new.idMateriel IS NULL THEN
    SELECT seq_materiel.NEXTVAL INTO :new.idMateriel FROM dual;
  END IF;
END;
/

-- 9. HistoriqueAction
CREATE TABLE HistoriqueAction (
    idAction NUMBER PRIMARY KEY,
    action VARCHAR2(255) NOT NULL,
    dateHeure TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    idAdmin NUMBER,
    CONSTRAINT fk_historique_admin FOREIGN KEY (idAdmin) REFERENCES Administrateur(idAdmin) ON DELETE SET NULL
);

CREATE SEQUENCE seq_historique START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER trg_historique 
BEFORE INSERT ON HistoriqueAction 
FOR EACH ROW 
BEGIN
  IF :new.idAction IS NULL THEN
    SELECT seq_historique.NEXTVAL INTO :new.idAction FROM dual;
  END IF;
END;
/

-- 10. Table d'association : ParticipationEvenement (Membre <-> Evenement)
CREATE TABLE ParticipationEvenement (
    idMembre NUMBER,
    idEvenement NUMBER,
    dateParticipation DATE,
    statutPresence VARCHAR2(50),
    role VARCHAR2(50),
    PRIMARY KEY (idMembre, idEvenement),
    CONSTRAINT fk_part_membre FOREIGN KEY (idMembre) REFERENCES Membre(idMembre) ON DELETE CASCADE,
    CONSTRAINT fk_part_evenement FOREIGN KEY (idEvenement) REFERENCES Evenement(idEvenement) ON DELETE CASCADE
);

-- 11. Table d'association : PresenceReunion (Membre <-> Reunion)
CREATE TABLE PresenceReunion (
    idMembre NUMBER,
    idReunion NUMBER,
    datePresence DATE,
    statutPresence VARCHAR2(50),
    PRIMARY KEY (idMembre, idReunion),
    CONSTRAINT fk_pres_membre FOREIGN KEY (idMembre) REFERENCES Membre(idMembre) ON DELETE CASCADE,
    CONSTRAINT fk_pres_reunion FOREIGN KEY (idReunion) REFERENCES Reunion(idReunion) ON DELETE CASCADE
);

-- Insertion de données de test (Administrateur par défaut)
INSERT INTO Administrateur (nom, prenom, login, motDePasse) 
VALUES ('Admin', 'Principal', 'admin', 'admin123');
COMMIT;

