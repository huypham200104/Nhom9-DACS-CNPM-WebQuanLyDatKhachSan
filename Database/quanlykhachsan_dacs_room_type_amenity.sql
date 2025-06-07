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
-- Table structure for table `room_type_amenity`
--

DROP TABLE IF EXISTS `room_type_amenity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_type_amenity` (
  `room_type_id` char(36) NOT NULL,
  `amenity_id` int NOT NULL,
  KEY `FKqhq7srlmtscp8y91y7ivp9lj0` (`amenity_id`),
  KEY `FKsdlek5m3gfai4wfh4885lj0gh` (`room_type_id`),
  CONSTRAINT `FKqhq7srlmtscp8y91y7ivp9lj0` FOREIGN KEY (`amenity_id`) REFERENCES `amenity` (`amenity_id`),
  CONSTRAINT `FKsdlek5m3gfai4wfh4885lj0gh` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_type_amenity`
--

LOCK TABLES `room_type_amenity` WRITE;
/*!40000 ALTER TABLE `room_type_amenity` DISABLE KEYS */;
INSERT INTO `room_type_amenity` VALUES ('1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b',1),('1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b',2),('1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b',3),('1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b',4),('1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b',5),('1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b',6),('1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b',7),('1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b',8),('1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b',9),('1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b',10),('2f3b2e8d-5c1d-4a8c-8a9e-6c4ef31f5c5a',2),('2f3b2e8d-5c1d-4a8c-8a9e-6c4ef31f5c5a',4),('2f3b2e8d-5c1d-4a8c-8a9e-6c4ef31f5c5a',7),('2f3b2e8d-5c1d-4a8c-8a9e-6c4ef31f5c5a',8);
/*!40000 ALTER TABLE `room_type_amenity` ENABLE KEYS */;
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
