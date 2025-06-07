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
-- Table structure for table `image_room`
--

DROP TABLE IF EXISTS `image_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_room` (
  `image_room_id` int NOT NULL AUTO_INCREMENT,
  `image_room_url` varchar(255) NOT NULL,
  `room_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`image_room_id`),
  KEY `FK_image_room_room` (`room_id`),
  CONSTRAINT `FK_image_room_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`),
  CONSTRAINT `FKaxqwhp7syt0s1k8dhq0qxbmo8` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_room`
--

LOCK TABLES `image_room` WRITE;
/*!40000 ALTER TABLE `image_room` DISABLE KEYS */;
INSERT INTO `image_room` VALUES (14,'aa18132b-0297-4644-9fe0-a33c0cf491d6_RenaissanceRiversideHotelSaigon_t.jpg','3669d28d-eae6-4156-b6f7-49aaac858901'),(15,'442ade01-d43d-451c-895b-f4c1fbc847a3_TheReverieSaigon_1.jpg','c56d5d31-eb6b-480c-abef-8329a37d4b81'),(16,'5516a11a-e7e1-4418-8ce9-455c36aa7e17_hotel-in-vietnam.jpg','a25dafc0-2d11-453b-96fc-b1c16b4d4749'),(17,'32686b94-19e0-4508-b376-90230a19e279_kt.jpg','e5a0e75d-29ff-4b8f-be6c-6aeb1ff5f792'),(18,'573cec7e-4262-4d42-9142-6030178d402a_saigonpink1.jpg','b0507210-445a-4b81-a342-99ba40b3e03d'),(19,'d46d9f93-171d-4a06-ac44-db14ba447cfe_saigonpink2.jpg','743310d7-816b-47fb-a92e-d84690cb226a'),(20,'acea933a-92f4-488b-a78f-24f80e374318_TheReverieSaigon_1.jpg','9d14bea7-9a95-425b-a103-2a8f5dad866c');
/*!40000 ALTER TABLE `image_room` ENABLE KEYS */;
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
