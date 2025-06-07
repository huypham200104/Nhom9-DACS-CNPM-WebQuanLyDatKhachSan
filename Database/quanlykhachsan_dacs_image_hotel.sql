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
-- Table structure for table `image_hotel`
--

DROP TABLE IF EXISTS `image_hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_hotel` (
  `image_hotel_id` char(36) NOT NULL,
  `image_hotel_url` varchar(255) DEFAULT NULL,
  `hotel_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`image_hotel_id`),
  KEY `FK_hotel_image` (`hotel_id`),
  CONSTRAINT `FK_hotel_image` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`) ON DELETE CASCADE,
  CONSTRAINT `FKckbwc910yk2vmulc4bt1q4bkk` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_hotel`
--

LOCK TABLES `image_hotel` WRITE;
/*!40000 ALTER TABLE `image_hotel` DISABLE KEYS */;
INSERT INTO `image_hotel` VALUES ('69105e71-5cf7-4078-9f4a-21f4d0aacea4','c61cc8ab-c84a-404c-9eaf-960818801735_k&t.jpg','a7f73fab-c900-4a84-92c1-d2c958df24ae'),('b0f3c721-0261-406f-8de7-cfe8a0e70daa','7a854699-cef2-4393-8996-b6a66c2ae14a_20016842-3078abf5cf90a3ec8b59453f05737775.jpg','2279a26a-8b58-49a0-984f-8026e746a60f'),('cc969b01-aacf-4337-8d8c-ff81189dfbc1','1da4fdbb-9e8a-48b6-a417-133eee79979f_hanoi1cr-scaled.jpg','77d8a88b-3ca8-429d-a86b-35f168884439'),('f2904740-b6b6-402a-8d0a-6d9fa81b3267','6c211d20-0a85-43fb-af56-e018c72a1cce_saigonpink.jpg','dc7ecb2a-0a9a-4d88-bb6c-5cbd5ca158f1'),('f9b5ab81-77bf-4179-901d-72f68faee142','8236af69-7915-447b-bd44-af53959d7caa_BlueMoon.jpg','75bb8924-14ef-4c82-9a16-c777da77a0fd');
/*!40000 ALTER TABLE `image_hotel` ENABLE KEYS */;
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
