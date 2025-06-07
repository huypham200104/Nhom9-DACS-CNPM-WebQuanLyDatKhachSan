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
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `payment_id` char(36) NOT NULL,
  `amount` bigint NOT NULL,
  `payment_date` datetime(6) NOT NULL,
  `payment_details` varchar(2000) DEFAULT NULL,
  `payment_method` enum('BANK_TRANSFER','CASH','CREDIT_CARD','MOMO','OTHER','VNPAY') NOT NULL,
  `provider_response` varchar(2000) DEFAULT NULL,
  `status` enum('COMPLETED','FAILED','PARTIALLY_REFUNDED','PENDING','REFUNDED') NOT NULL,
  `transaction_code` varchar(100) DEFAULT NULL,
  `booking_id` char(36) NOT NULL,
  PRIMARY KEY (`payment_id`),
  UNIQUE KEY `unique_transaction_code` (`transaction_code`),
  KEY `FKqewrl4xrv9eiad6eab3aoja65` (`booking_id`),
  CONSTRAINT `FKqewrl4xrv9eiad6eab3aoja65` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES ('71e4f700-a0ff-486e-859d-71deda4e7e4a',270000,'2025-06-07 10:37:59.746779','Booking room 3669d28d-eae6-4156-b6f7-49aaac858901 from 2025-07-01 to 2025-07-03','VNPAY',NULL,'COMPLETED','69144975','a71b62e2-2569-497e-9184-fd9004f21d34'),('977246ce-d119-453a-9f7b-569881b48446',150000,'2025-06-07 11:39:07.565635','Booking room 3669d28d-eae6-4156-b6f7-49aaac858901 from 2025-06-11 to 2025-06-12','VNPAY',NULL,'COMPLETED','02699535','c0cf42da-9a53-4f80-b43f-c22ca820388a'),('ba2ececa-c37e-46b4-ba3e-8ded8bcfe09e',50000,'2025-06-05 20:29:15.257163','Booking room c56d5d31-eb6b-480c-abef-8329a37d4b81 from 2025-06-05 to 2025-06-06','VNPAY',NULL,'COMPLETED','95452971','b1b65e8f-09be-48eb-b42b-e4e79d546988'),('f142bf0f-986d-40e5-9f8b-44a3ca2c0f96',3360000,'2025-06-07 13:54:50.584008','Booking room 3669d28d-eae6-4156-b6f7-49aaac858901 from 2025-07-11 to 2025-08-08','VNPAY',NULL,'COMPLETED','56335991','bdc4eb53-6695-4c86-86f5-b66918b232de'),('f728ea1f-714f-47e6-9d0b-ffb2c79cd5c9',1080000,'2025-06-07 00:27:19.568621','Booking room 743310d7-816b-47fb-a92e-d84690cb226a from 2025-06-07 to 2025-06-16','VNPAY',NULL,'COMPLETED','10496316','6ed3fc19-d7fc-4d29-847d-8fec91250505');
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
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
