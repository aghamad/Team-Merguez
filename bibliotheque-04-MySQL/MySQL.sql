DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS livre       CASCADE;
DROP TABLE IF EXISTS membre      CASCADE;

CREATE TABLE membre (idMembre   INTEGER(3) AUTO_INCREMENT  CHECK (idMembre > 0),
                     nom        VARCHAR(100)  NOT NULL,
                     telephone  BIGINT(10),
					 limitePret INTEGER(2)   CHECK (limitePret > 0 AND limitePret <= 10),
					 CONSTRAINT cleMembre    PRIMARY KEY (idMembre),
					 CONSTRAINT limiteNbPret CHECK (nbPret <= limitePret));

CREATE TABLE livre (idLivre         INTEGER(3) AUTO_INCREMENT   CHECK (idLivre > 0),
                    titre           VARCHAR(100)   NOT NULL,
					auteur          VARCHAR(100)   NOT NULL,
					dateAcquisition TIMESTAMP(3)  NOT NULL,
					idMembre        INTEGER(3),
					datePret        TIMESTAMP(3)  NULL,
					CONSTRAINT      cleLivre      PRIMARY KEY (idLivre),
					CONSTRAINT      refMembre FOREIGN KEY (idMembre) REFERENCES membre (idMembre));
					
CREATE TABLE pret (idPret INTEGER(3) AUTO_INCREMENT CHECK(idPret > 0),
					idMembre INTEGER(3) NOT NULL CHECK(idMembre > 0),
					idLivre INTEGER(3) NOT NULL CHECK(idLivre > 0),
					datePret TIMESTAMP(3),
					dateRetour TIMESTAMP(3) NULL,
					CONSTRAINT clePrimairePret PRIMARY KEY (idPret),
					CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre),
					CONSTRAINT refPretLivre FOREIGN KEY (idLivre) REFERENCES livre(idLivre));

CREATE TABLE reservation (idReservation   INTEGER(3) AUTO_INCREMENT,
                          idMembre        INTEGER(3),
						  idLivre         INTEGER(3),
						  dateReservation TIMESTAMP(3),
						  CONSTRAINT      clePrimaireReservation  PRIMARY KEY (idReservation),
						  CONSTRAINT      cleEtrangereReservation UNIQUE (idMembre, idLivre),
						  CONSTRAINT      refReservationMembre    FOREIGN KEY (idMembre) REFERENCES membre (idMembre) ON DELETE CASCADE,
			              CONSTRAINT      refReservationLivre     FOREIGN KEY (idLivre)  REFERENCES livre (idLivre)   ON DELETE CASCADE);