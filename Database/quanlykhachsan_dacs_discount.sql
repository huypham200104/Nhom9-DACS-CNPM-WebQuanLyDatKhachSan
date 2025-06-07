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
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discount` (
  `discount_id` char(36) NOT NULL,
  `discount_code` varchar(255) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `minimum_booking_amount` bigint DEFAULT NULL,
  `name_discount` varchar(255) DEFAULT NULL,
  `number_of_uses` int DEFAULT NULL,
  `percentage` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `status` enum('ACTIVE','EXPIRED','INACTIVE') DEFAULT NULL,
  `used_count` int DEFAULT NULL,
  `hotel_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`discount_id`),
  KEY `FKhje1ml7a7ina1n7i96nc7utvl` (`hotel_id`),
  CONSTRAINT `FKhje1ml7a7ina1n7i96nc7utvl` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discount`
--

LOCK TABLES `discount` WRITE;
/*!40000 ALTER TABLE `discount` DISABLE KEYS */;
INSERT INTO `discount` VALUES ('44ffb7af-e974-4606-b0d2-0eb00f0c66f9','abcd','2025-06-26',100000,'asdhjkad',10,20,'2025-06-07','ACTIVE',0,'77d8a88b-3ca8-429d-a86b-35f168884439'),('ff514e96-b633-4620-b338-a792afb08add','abc','2030-01-01',300000,'Mùa hè sôi động',10,10,'2000-01-01','ACTIVE',1,'77d8a88b-3ca8-429d-a86b-35f168884439');
/*!40000 ALTER TABLE `discount` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-07 18:53:12
