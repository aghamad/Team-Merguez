DROP TABLE reservation CASCADE CONSTRAINTS PURGE;
DROP TABLE livre CASCADE CONSTRAINTS PURGE;
DROP TABLE membre CASCADE CONSTRAINTS PURGE;



CREATE TABLE membre 
(
    idMembre        NUMBER(3) CHECK(idMembre > 0), 
    nom             VARCHAR(10) NOT NULL, 
    telephone       NUMBER(10) , 
    limitePret      NUMBER(2) CHECK(limitePret > 0 AND limitePret <= 10) ,
    nbpret          NUMBER(2) DEFAULT 0 CHECK(nbpret >= 0) , 
    CONSTRAINT cleMembre PRIMARY KEY (idMembre), 
    CONSTRAINT limiteNbPret CHECK(nbpret <= limitePret) 
);

CREATE TABLE livre 
(
	idLivre         NUMBER(3) CHECK(idLivre > 0), 
    titre           VARCHAR(10) NOT NULL, 
    auteur          VARCHAR(10) NOT NULL, 
    dateAcquisition DATE NOT NULL, 
    idMembre        NUMBER(3), 
    datePret        DATE, 
    CONSTRAINT cleLivre PRIMARY KEY (idLivre), 
    CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre)
);

CREATE TABLE reservation 
( 
    idReservation   NUMBER(3) , 
    idMembre        NUMBER(3) , 
    idLivre         NUMBER(3) , 
    dateReservation DATE ,
    CONSTRAINT cleReservation PRIMARY KEY (idReservation) , 
    CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre) , 
    CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre (idMembre)
    ON DELETE CASCADE , 
    CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre (idLivre)
	ON DELETE CASCADE 
)