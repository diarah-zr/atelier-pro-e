CREATE TABLE Employe (
    numero_Employe INT PRIMARY KEY,
    nom_Employe VARCHAR(50) NOT NULL,
    prenom_Employe VARCHAR(50) NOT NULL,
    password_Employe VARCHAR(255) NOT NULL,
    poste VARCHAR(50),
    date_arrivee DATE,
    date_depart DATE,
    numero_Ligue INT NULL
);

CREATE TABLE Ligue (
    numero_Ligue INT PRIMARY KEY,
    nom_Ligue VARCHAR(50) NOT NULL,
    numero_Admin INT NOT NULL
);


ALTER TABLE Employe
ADD CONSTRAINT fk_employe_ligue
FOREIGN KEY (numero_Ligue)
REFERENCES Ligue(numero_Ligue)
ON DELETE SET NULL
ON UPDATE CASCADE;

ALTER TABLE Ligue
ADD CONSTRAINT fk_ligue_admin
FOREIGN KEY (numero_Admin)
REFERENCES Employe(numero_Employe)
ON DELETE RESTRICT
ON UPDATE CASCADE;