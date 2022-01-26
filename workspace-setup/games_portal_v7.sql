-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.6.3-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for games_portal
CREATE DATABASE IF NOT EXISTS `games_portal` /*!40100 DEFAULT CHARACTER SET utf8mb3 */;
USE `games_portal`;

-- Dumping structure for table games_portal.active_logins
CREATE TABLE IF NOT EXISTS `active_logins` (
  `Login_id` varchar(38) NOT NULL,
  `Player_id` varchar(38) NOT NULL,
  `Login_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `User_name` varchar(30) NOT NULL,
  UNIQUE KEY `Uniikki` (`Player_id`) USING BTREE,
  UNIQUE KEY `Uniikki2` (`Login_id`),
  UNIQUE KEY `Player_name` (`User_name`) USING BTREE,
  CONSTRAINT `Player_id_FK` FOREIGN KEY (`Player_id`) REFERENCES `users` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table games_portal.active_logins: ~0 rows (approximately)
DELETE FROM `active_logins`;
/*!40000 ALTER TABLE `active_logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `active_logins` ENABLE KEYS */;

-- Dumping structure for table games_portal.available_games
CREATE TABLE IF NOT EXISTS `available_games` (
  `Id` int(11) NOT NULL DEFAULT 0,
  `Name_EN` varchar(50) NOT NULL DEFAULT 'TicTacToe',
  `Name_CH` varchar(50) NOT NULL DEFAULT 'TicTacToe',
  `Name_ES` varchar(50) NOT NULL DEFAULT 'TicTacToe',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table games_portal.available_games: ~0 rows (approximately)
DELETE FROM `available_games`;
/*!40000 ALTER TABLE `available_games` DISABLE KEYS */;
/*!40000 ALTER TABLE `available_games` ENABLE KEYS */;

-- Dumping structure for table games_portal.pending_registrations
CREATE TABLE IF NOT EXISTS `pending_registrations` (
  `Id` varchar(50) NOT NULL,
  `TimesSent` int(11) DEFAULT NULL,
  `Password` varchar(50) NOT NULL,
  `Email` varchar(140) NOT NULL,
  `UserName` varchar(50) DEFAULT NULL,
  `Sent` datetime DEFAULT NULL,
  `Expires` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table games_portal.pending_registrations: ~0 rows (approximately)
DELETE FROM `pending_registrations`;
/*!40000 ALTER TABLE `pending_registrations` DISABLE KEYS */;
/*!40000 ALTER TABLE `pending_registrations` ENABLE KEYS */;

-- Dumping structure for table games_portal.played_games
CREATE TABLE IF NOT EXISTS `played_games` (
  `Game_number` int(11) NOT NULL DEFAULT 0,
  `Player1_id` varchar(38) NOT NULL,
  `Player2_id` varchar(38) NOT NULL,
  `Winner_id` varchar(38) NOT NULL,
  KEY `GameId_FK` (`Game_number`) USING BTREE,
  KEY `Player1_FK` (`Player1_id`) USING BTREE,
  KEY `Player2_FK` (`Player2_id`) USING BTREE,
  KEY `Winner_FK` (`Winner_id`) USING BTREE,
  CONSTRAINT `GameId_FK` FOREIGN KEY (`Game_number`) REFERENCES `available_games` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `Player1_FK` FOREIGN KEY (`Player1_id`) REFERENCES `users` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `Player2_FK` FOREIGN KEY (`Player2_id`) REFERENCES `users` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `Winner_FK` FOREIGN KEY (`Winner_id`) REFERENCES `users` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table games_portal.played_games: ~0 rows (approximately)
DELETE FROM `played_games`;
/*!40000 ALTER TABLE `played_games` DISABLE KEYS */;
/*!40000 ALTER TABLE `played_games` ENABLE KEYS */;

-- Dumping structure for table games_portal.users
CREATE TABLE IF NOT EXISTS `users` (
  `UserName` varchar(15) NOT NULL,
  `Id` varchar(38) NOT NULL,
  `Email` varchar(130) DEFAULT NULL,
  `Secret` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `Created` datetime NOT NULL DEFAULT current_timestamp(),
  `Modified` datetime DEFAULT NULL,
  `LastLogin` datetime DEFAULT NULL,
  `Status` enum('ACTIVE','PASSIVE','BLOCKED') NOT NULL DEFAULT 'ACTIVE',
  `Force_password_change` enum('true','false') NOT NULL DEFAULT 'false',
  `tult` varchar(50) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Uniikki_username` (`UserName`) USING BTREE,
  UNIQUE KEY `Uniikki_email` (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table games_portal.users: ~0 rows (approximately)
DELETE FROM `users`;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
