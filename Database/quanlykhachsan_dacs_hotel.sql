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
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel` (
  `hotel_id` char(36) NOT NULL,
  `approval_status` enum('APPROVED','PENDING','REJECTED') DEFAULT NULL,
  `approved_at` date DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `hotel_name` varchar(255) DEFAULT NULL,
  `hotel_rating` bigint DEFAULT NULL,
  `house_number` varchar(255) DEFAULT NULL,
  `latitude` bigint DEFAULT NULL,
  `longitude` bigint DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `ward` varchar(255) DEFAULT NULL,
  `approved_by` char(36) DEFAULT NULL,
  PRIMARY KEY (`hotel_id`),
  KEY `FK4jo84malmw7b86rhepb7twn27` (`approved_by`),
  CONSTRAINT `FK4jo84malmw7b86rhepb7twn27` FOREIGN KEY (`approved_by`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel`
--

LOCK TABLES `hotel` WRITE;
/*!40000 ALTER TABLE `hotel` DISABLE KEYS */;
INSERT INTO `hotel` VALUES ('2279a26a-8b58-49a0-984f-8026e746a60f','APPROVED',NULL,'Sài Gòn','2025-05-27 17:07:53.501668','Bình Thạnh','Khách Sạn Bạch Đằng',5,'341',1678452312,10822345623,'Điện Biên Phủ','2025-05-27 17:08:07.420740','Phường 25',NULL),('75bb8924-14ef-4c82-9a16-c777da77a0fd','APPROVED',NULL,'Nha Trang','2025-06-05 16:17:18.388480','Cam Ranh','Blue moon',5,'1',NULL,NULL,'Lê Duẫn','2025-06-05 16:17:22.566537','Cam Lộc',NULL),('77d8a88b-3ca8-429d-a86b-35f168884439','APPROVED',NULL,'Đà Nẵng','2025-05-26 22:26:54.451561','Sơn Trà','Khách Sạn Biển Xanh',5,'123',16784523,108223456,'Võ Nguyên Giáp','2025-05-26 22:29:47.017451','Phước Mỹ',NULL),('a7f73fab-c900-4a84-92c1-d2c958df24ae','APPROVED',NULL,'Hồ Chí Minh','2025-06-05 16:39:17.816103','Tân Bình','K&T',5,'123',NULL,NULL,'Hoàng Văn Thụ','2025-06-05 16:41:34.781183','9',NULL),('dc7ecb2a-0a9a-4d88-bb6c-5cbd5ca158f1','APPROVED',NULL,'Hồ Chí Minh','2025-06-05 16:56:09.689218','Quận 4','Sài Gòn Pink',5,'271',NULL,NULL,'Vĩnh Khánh','2025-06-07 08:20:31.810924','phường 8',NULL);
/*!40000 ALTER TABLE `hotel` ENABLE KEYS */;
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
