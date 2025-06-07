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
-- Table structure for table `staff_hotel`
--

DROP TABLE IF EXISTS `staff_hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff_hotel` (
  `staff_hotel_id` char(36) NOT NULL,
  `end_date` date DEFAULT NULL,
  `position` enum('MANAGER','RECEPTIONIST') DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `hotel_id` char(36) DEFAULT NULL,
  `user_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`staff_hotel_id`),
  KEY `FK2y5wn6c9bu4liiylxbe1qtmod` (`hotel_id`),
  KEY `FK7e6g3r8xhbgqm5s0vx8iof0na` (`user_id`),
  CONSTRAINT `FK2y5wn6c9bu4liiylxbe1qtmod` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`),
  CONSTRAINT `FK7e6g3r8xhbgqm5s0vx8iof0na` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_hotel`
--

LOCK TABLES `staff_hotel` WRITE;
/*!40000 ALTER TABLE `staff_hotel` DISABLE KEYS */;
INSERT INTO `staff_hotel` VALUES ('854a4e65-bd57-454b-b403-0c8dfdc9a52f','2030-12-26','MANAGER','2025-06-05','a7f73fab-c900-4a84-92c1-d2c958df24ae','cdff6cc5-c4b8-42ac-a20f-b622ae7cf10b'),('9fdb46a7-a7fe-44b1-8b0d-10b63cf5cc05','2030-01-01','MANAGER','2024-01-01','2279a26a-8b58-49a0-984f-8026e746a60f','6e821593-37fc-4850-8ea6-5c8256ad0e9d'),('be9ea68b-60f2-413f-8f19-980f1199a0e2','2025-07-12','MANAGER','2025-06-06','dc7ecb2a-0a9a-4d88-bb6c-5cbd5ca158f1','4afc70f4-1281-4813-ae63-2120bc589bd9'),('dcded369-ed30-43f9-87cd-ce72bff57624','2030-01-01','MANAGER','2024-01-01','77d8a88b-3ca8-429d-a86b-35f168884439','fa10b2f6-4c17-4769-8242-796824b892b7'),('f00d2f9c-82bb-4eb6-a97e-568d23bd204b','2030-01-01','MANAGER','2024-01-01','75bb8924-14ef-4c82-9a16-c777da77a0fd','cc3f8875-a28e-473e-adb6-c31f7afae4ea');
/*!40000 ALTER TABLE `staff_hotel` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-07 18:53:13
