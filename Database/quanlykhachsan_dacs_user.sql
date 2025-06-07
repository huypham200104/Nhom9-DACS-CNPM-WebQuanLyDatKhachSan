-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: quanlykhachsan_dacs
-- ------------------------------------------------------
-- Server version	9.1.0

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

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` char(36) NOT NULL,
  `created_at` date DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `google_id` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CUSTOMER','STAFF') DEFAULT NULL,
  `user_status` enum('ACTIVE','INACTIVE','SUSPENDED') DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UKlfismxng57avbbo40y5tbppf7` (`google_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('074324fd-2437-4062-8073-d51d36e11602','2025-05-28','admin@gmail.com',NULL,'$2a$10$2FZTDN.vq6Pyrj3SeKAF9e07DZsHcCAAIsGjGD/hPwvsCjyfM4iZK','ADMIN','ACTIVE'),('4afc70f4-1281-4813-ae63-2120bc589bd9','2025-06-06','saigonpink@gmail.com',NULL,'$2a$10$DDljia1EdA6k2ZaAwlPK9.iitOFUQEy.CMde7ugqK30MwVaz41jAG','STAFF','ACTIVE'),('6e821593-37fc-4850-8ea6-5c8256ad0e9d','2025-05-27','st2@gmail.com',NULL,'$2a$10$uH9jNQgqqGjBUgzh.xdu.eIT1CGbz8w2Y6nOVq0oKqINjAjHZU2.q','STAFF','ACTIVE'),('94cbe3cf-a146-48d6-9d99-faa0fe609143','2025-05-26','ngochuy200104@gmail.com',NULL,'$2a$10$E3n/XFbrupywU5u56fZHu.iUbSmsF7lJ9rhoYzQOpQojDO7IU3/yy','CUSTOMER','ACTIVE'),('cc3f8875-a28e-473e-adb6-c31f7afae4ea','2025-06-05','bluemoonstaff@gmail.com',NULL,'$2a$10$n6SN2sUA57YIUabBhfmXrOHve1AKebNGNoqwRVWlD5K2rRuV09fkC','STAFF','ACTIVE'),('cdff6cc5-c4b8-42ac-a20f-b622ae7cf10b','2025-06-05','ktstaff@gmail.com',NULL,'$2a$10$.EQQKObUhRHrpn5CGiKnoeFl0P7pCNA4cx4YLTB9gep8EWqweI9gK','STAFF','ACTIVE'),('de8cf77e-f6f5-44ff-9cbc-8cad502c1dcc','2025-06-07','huyphamforedu@gmail.com',NULL,'oauth2_user','CUSTOMER','ACTIVE'),('f9cb0536-fe95-4312-9dae-61ab79aad2ad','2025-05-27','pnhwagttn@gmail.com',NULL,'oauth2_user','CUSTOMER','ACTIVE'),('fa10b2f6-4c17-4769-8242-796824b892b7','2025-05-26','st1@gmail.com',NULL,'$2a$10$bejmkhmR9/rVxVty9WHUG.iKj0rXaX.ZB2HGbboHtyDKYHFeiw8.i','STAFF','ACTIVE');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-07 18:53:11
