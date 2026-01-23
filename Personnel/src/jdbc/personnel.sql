CREATE TABLE Employe (
    numero_Employe INT PRIMARY KEY,
    nom_Employe VARCHAR(50) NOT NULL,
    prenom_Employe VARCHAR(50) NOT NULL,
    password_Employe VARCHAR(255) NOT NULL,
    poste VARCHAR(50),
    date_arrivee DATE,
    date_depart DATE,
    numero_Ligues INT NULL
);

CREATE TABLE Ligues (
    numero_Ligues INT PRIMARY KEY,
    nom_Ligue VARCHAR(50) NOT NULL,
    numero_Employe INT NOT NULL
);


ALTER TABLE Employe
ADD CONSTRAINT fk_employe_ligue
FOREIGN KEY (numero_Ligues)
REFERENCES Ligues(numero_Ligues)
ON DELETE SET NULL
ON UPDATE CASCADE;

ALTER TABLE Ligues
ADD CONSTRAINT fk_ligue_employe
FOREIGN KEY (numero_Employe)
REFERENCES Employe(numero_Employe)
ON DELETE RESTRICT
ON UPDATE CASCADE;