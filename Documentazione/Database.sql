-- MySQL 10.13  Distrib 8.0.33, for macos13 (arm64)
--
-- Host: localhost    Database: aulery
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP DATABASE IF EXISTS aulery;
CREATE DATABASE aulery;
use aulery;

--
-- Table structure
--
CREATE TABLE `amministratore` (
	`username` varchar(255) NOT NULL,
	`password` varchar(1000) NOT NULL,
	`ID` int NOT NULL AUTO_INCREMENT,
    `version` int DEFAULT '0',
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `area` (
	`ID` int NOT NULL AUTO_INCREMENT,
	`nome` varchar(255) NOT NULL,
	`descrizione` text NOT NULL,
	`version` int DEFAULT '0',
	PRIMARY KEY (`ID`),
	UNIQUE KEY `nome_UNIQUE` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `amministratoreRest` (
	`ID` int NOT NULL AUTO_INCREMENT,
	`username` varchar(500) NOT NULL,
	`password` varchar(500) NOT NULL,
	`token` varchar(45) DEFAULT NULL,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `aula` (
	`ID` int NOT NULL AUTO_INCREMENT,
	`nome` varchar(255) NOT NULL,
	`note` text NOT NULL,
	`edificio` varchar(50) NOT NULL,
	`piano` int NOT NULL,
	`zona` varchar(50) NOT NULL,
	`capienza` int NOT NULL,
	`preseElettriche` int NOT NULL DEFAULT '0',
	`areaId` int NOT NULL,
	`preseDiRete` int NOT NULL DEFAULT '0',
	`attrezzature` varchar(45) DEFAULT NULL,
	`version` int DEFAULT '0',
	PRIMARY KEY (`ID`),
	UNIQUE KEY `nome_UNIQUE` (`nome`),
	KEY `areaId` (`areaId`),
	CONSTRAINT `aula_area` FOREIGN KEY (`areaId`) REFERENCES `area` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `corso` (
	`ID` int NOT NULL AUTO_INCREMENT,
	`nome` varchar(255) NOT NULL,
	`areaId` int NOT NULL,
	`version` varchar(45) DEFAULT '0',
	PRIMARY KEY (`ID`),
	KEY `areaId` (`areaId`),
	CONSTRAINT `corso_area` FOREIGN KEY (`areaId`) REFERENCES `area` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `evento` (
	`ID` int NOT NULL AUTO_INCREMENT,
	`nome` varchar(255) NOT NULL,
	`descrizione` text NOT NULL,
	`data` date NOT NULL,
	`oraInizio` time NOT NULL,
	`oraFine` time NOT NULL,
	`tipologiaEvento` varchar(255) NOT NULL,
	`personaId` int NOT NULL,
	`aulaId` int NOT NULL,
	`corsoId` int DEFAULT NULL,
	`version` int DEFAULT '0',
	`eventoRicorrenteId` int DEFAULT NULL,
	PRIMARY KEY (`ID`),
	KEY `personaId` (`personaId`),
	KEY `aulaId` (`aulaId`),
	KEY `evento_eventoRicorrente_idx` (`eventoRicorrenteId`),
	CONSTRAINT `evento_aula` FOREIGN KEY (`aulaId`) REFERENCES `aula` (`ID`),
	CONSTRAINT `evento_eventoRicorrente` FOREIGN KEY (`eventoRicorrenteId`) REFERENCES `eventoricorrente` (`ID`),
	CONSTRAINT `evento_persona` FOREIGN KEY (`personaId`) REFERENCES `persona` (`ID`),
	CONSTRAINT `verifica_ora_evento` CHECK ((`oraFine` >= `oraInizio`))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `eventoricorrente` (
	`ID` int NOT NULL AUTO_INCREMENT,
	`ricorrenza` varchar(50) NOT NULL,
	`dataFine` date NOT NULL,
	`version` int DEFAULT '0',
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `persona` (
	`ID` int NOT NULL AUTO_INCREMENT,
	`nome` varchar(50) NOT NULL,
	`cognome` varchar(50) NOT NULL,
	`email` varchar(50) NOT NULL,
	`version` int DEFAULT '0',
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data
--
INSERT INTO `amministratore` VALUES
	('simone','d0b11597c8b2c78eed7249c406872ee7e1ff2cfd1bb2941891f2d65399fdcfd5eca0bc92b59cc668fb4a5b8e0425a4f5',1, 0),
	('antonio','d0b11597c8b2c78eed7249c406872ee7e1ff2cfd1bb2941891f2d65399fdcfd5eca0bc92b59cc668fb4a5b8e0425a4f5',2, 0),
	('marco','d0b11597c8b2c78eed7249c406872ee7e1ff2cfd1bb2941891f2d65399fdcfd5eca0bc92b59cc668fb4a5b8e0425a4f5',3, 0);

INSERT INTO `amministratoreRest` VALUES 
	(1,'simone','d74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1','a2ee826a-8b45-4ed7-a2f2-2dfbd0603e7f');

INSERT INTO `area` VALUES 
	(1,'Disim','descrizione DISIM',0),
	(2,'Discab','descrizione DISCAB',0),
	(3,'Dsu','descrizione DSU',0),
	(4,'Mesva','descrizione MESVA',0);

INSERT INTO `aula` VALUES 
	(1,'C1.16','Aula per lezioni','COPPITO2',1,'Ospedale',46,7,1,4,'PROIETTORE',0),
	(2,'Aula Rossa','Aula per tenere le lezioni','COPPITO1',2,'Ospedale',100,100,1,5,'MICROFONOAFILO,PCFISSO,IMPIANTOAUDIO',0),
	(3,'C1.10','Aula per le lezioni','COPPITO2',1,'Ospedale',126,10,1,3,'PCFISSO',0),
	(4,'C3.25','Aula per lezioni','COPPITO2',3,'Ospedale',30,5,2,3,'PROIETTORE',0),
	(5,'C3.26','Aula per lezioni','COPPITO2',3,'Ospedale',34,7,2,3,'SCHERMOMOTORIZZATO',0),
	(6,'2.A','Aula per eventi','BLOCCOC',2,'Ospedale',100,6,3,9,'IMPIANTOAUDIO',0),
	(7,'1.A','Aula per eventi','BLOCCOC',1,'Ospedale',150,25,3,5,'PROIETTORE',0),
	(8,'D1.13','Aula per lezioni','BLOCCOB',1,'Ospedale',50,6,4,3,'PROIETTORE',0),
	(9,'D2.22','Laboratorio','BLOCCOB',2,'Ospedale',30,40,4,35,'PROIETTORE',0);

INSERT INTO `corso` VALUES 
	(1,'Web Engineering',1,'0'),
	(2,'Fondamenti Anatomo',2,'0'),
	(3,'Disegno',3,'0'),
	(4,'Microbiologia',4,'0');

INSERT INTO `evento` VALUES 
	(1,'Web Engineering','Login Logout','2023-07-06','18:00:00','18:45:00','ESERCITAZIONE',1,2,1,0,NULL),
	(2,'Fondamenti Anatomo esame','Esame di fondamenti','2023-07-06','16:00:00','17:30:00','ESAME',3,4,2,0,NULL),
	(3,'Seminario di disegno','A presentare il seminario sarÃ  Salvador DÃ li','2023-07-06','16:00:00','17:30:00','SEMINARIO',2,2,3,0,NULL),
	(4,'Microbiologia','Lezione di microbiologia','2023-07-06','16:00:00','19:00:00','LEZIONE',1,3,4,0,NULL),
	(5,'Lauree','Lauree in psicologia','2023-07-06','08:00:00','13:00:00','LAUREE',2,5,NULL,0,NULL),
	(6,'Analisi Matematica','Lezione analisi','2023-07-06','11:20:00','18:00:00','LEZIONE',1,2,NULL,0,NULL),
	(7,'Sviluppo Web Avanzato','Lezione SWA','2023-07-05','09:45:00','12:45:00','LEZIONE',1,1,NULL,0,NULL);

INSERT INTO `persona` VALUES 
	(1,'Antonio','Valentino','antoniovalentino@emaill.com',0),
	(2,'Simone','Morisi','simonemorisi@email.com',0),
	(3,'Marco','D\'antonio','marcodantonio@email.com',0);
    
DELIMITER ;;
CREATE TRIGGER `evita_sovrapposizioni` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
	DECLARE count_overlap INT;
	SET count_overlap = (
	SELECT COUNT(*)
	FROM evento
	WHERE aulaId = NEW.aulaId
		AND data = NEW.data
		AND ((NEW.oraInizio < oraFine AND NEW.oraFine > oraInizio)
		OR (NEW.oraInizio >= oraInizio AND NEW.oraFine <= oraFine)
		OR (NEW.oraInizio <= oraInizio AND NEW.oraFine >= oraFine)));
	IF count_overlap > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sovrapposizione di eventi non consentita';
	END IF;
END;;
DELIMITER ;

DELIMITER ;;
CREATE TRIGGER `verifica_ora_evento` BEFORE INSERT ON `evento` FOR EACH ROW BEGIN
    IF NEW.oraFine < NEW.oraInizio THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'L\'ora di fine non può essere inferiore all\'ora di inizio.';
    END IF;
END;;
DELIMITER ;