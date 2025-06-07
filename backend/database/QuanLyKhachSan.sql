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
-- Table structure for table `amenity`
--

DROP TABLE IF EXISTS `amenity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amenity` (
  `amenity_id` int NOT NULL AUTO_INCREMENT,
  `amenity_name` varchar(255) DEFAULT NULL,
  `amenity_status` enum('AVAILABLE','UNAVAILABLE') DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`amenity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `booking_id` char(36) NOT NULL,
  `booking_status` enum('CANCELLED','CHECKED_IN','CHECKED_OUT','CONFIRMED','NO_SHOW','PAID','PENDING','REFUNDED') NOT NULL,
  `check_in_date` date NOT NULL,
  `check_out_date` date NOT NULL,
  `created_at` date DEFAULT NULL,
  `number_of_guests` int NOT NULL,
  `payment_method` enum('BANK_TRANSFER','CASH','CREDIT_CARD','MOMO','OTHER','VNPAY') NOT NULL,
  `price_before_discount` bigint NOT NULL,
  `special_requests` varchar(2000) DEFAULT NULL,
  `total_price` bigint NOT NULL,
  `updated_at` date DEFAULT NULL,
  `customer_id` char(36) DEFAULT NULL,
  `discount_id` char(36) DEFAULT NULL,
  `room_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`booking_id`),
  KEY `FKlnnelfsha11xmo2ndjq66fvro` (`customer_id`),
  KEY `FKg1g5k55pf72f5pftba35ark24` (`discount_id`),
  KEY `FKq83pan5xy2a6rn0qsl9bckqai` (`room_id`),
  CONSTRAINT `FKg1g5k55pf72f5pftba35ark24` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`discount_id`),
  CONSTRAINT `FKlnnelfsha11xmo2ndjq66fvro` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `FKq83pan5xy2a6rn0qsl9bckqai` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `booking_room`
--

DROP TABLE IF EXISTS `booking_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_room` (
  `room_id` char(36) NOT NULL,
  `booking_id` char(36) NOT NULL,
  KEY `FK9umnt0pjb1nf83qwoqry1cuc2` (`booking_id`),
  KEY `FK4e002f18klgu08ekxnav2rwr9` (`room_id`),
  CONSTRAINT `FK4e002f18klgu08ekxnav2rwr9` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`),
  CONSTRAINT `FK9umnt0pjb1nf83qwoqry1cuc2` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_id` char(36) NOT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `user_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `UKj7ja2xvrxudhvssosd4nu1o92` (`user_id`),
  CONSTRAINT `FKj8dlm21j202cadsbfkoem0s58` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `favorite_hotel`
--

DROP TABLE IF EXISTS `favorite_hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite_hotel` (
  `favorite_id` char(36) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `customer_id` char(36) NOT NULL,
  `hotel_id` char(36) NOT NULL,
  PRIMARY KEY (`favorite_id`),
  KEY `FK5kboae4kuwiu9ymqylok5che7` (`customer_id`),
  KEY `FK93rfi1sxxkxvgmr0gwcp4gd2r` (`hotel_id`),
  CONSTRAINT `FK5kboae4kuwiu9ymqylok5che7` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `FK93rfi1sxxkxvgmr0gwcp4gd2r` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `feedback_id` char(36) NOT NULL,
  `content` varchar(2000) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `feedback_rating` int NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `booking_id` char(36) NOT NULL,
  `customer_id` char(36) NOT NULL,
  `hotel_id` char(36) NOT NULL,
  PRIMARY KEY (`feedback_id`),
  KEY `FKa63vevie0kvm3aai9f0d226l3` (`booking_id`),
  KEY `FKpi2y2j7n01ypo49fone3knjry` (`customer_id`),
  KEY `FKt9lta62m3mgg7eh81ed3umcj2` (`hotel_id`),
  CONSTRAINT `FKa63vevie0kvm3aai9f0d226l3` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`),
  CONSTRAINT `FKpi2y2j7n01ypo49fone3knjry` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `FKt9lta62m3mgg7eh81ed3umcj2` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`),
  CONSTRAINT `feedback_chk_1` CHECK (((`feedback_rating` >= 1) and (`feedback_rating` <= 5)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `FKqewrl4xrv9eiad6eab3aoja65` (`booking_id`),
  CONSTRAINT `FKqewrl4xrv9eiad6eab3aoja65` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `revenue_statistics`
--

DROP TABLE IF EXISTS `revenue_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `revenue_statistics` (
  `statistic_id` char(36) NOT NULL,
  `average_rating` bigint DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `occupancy_rate` bigint DEFAULT NULL,
  `period_end` date NOT NULL,
  `period_start` date NOT NULL,
  `total_bookings` int DEFAULT NULL,
  `total_revenue` bigint NOT NULL,
  `hotel_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`statistic_id`),
  KEY `FK53gcj9onm228cio39hjreb0o1` (`hotel_id`),
  CONSTRAINT `FK53gcj9onm228cio39hjreb0o1` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `room_id` char(36) NOT NULL,
  `bed_type` enum('DOUBLE','KING','QUEEN','SINGLE') DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `description` text,
  `max_guests` int NOT NULL,
  `price` bigint NOT NULL,
  `room_size` bigint DEFAULT NULL,
  `room_status` varchar(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  `hotel_id` char(36) DEFAULT NULL,
  `room_type_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`room_id`),
  KEY `FKdosq3ww4h9m2osim6o0lugng8` (`hotel_id`),
  KEY `FKd468eq7j1cbue8mk20qfrj5et` (`room_type_id`),
  CONSTRAINT `FKd468eq7j1cbue8mk20qfrj5et` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`),
  CONSTRAINT `FKdosq3ww4h9m2osim6o0lugng8` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `room_type`
--

DROP TABLE IF EXISTS `room_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_type` (
  `room_type_id` char(36) NOT NULL,
  `room_type_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`room_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','CUSTOMER','STAFF') DEFAULT NULL,
  `user_status` enum('ACTIVE','INACTIVE','SUSPENDED') DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UKlfismxng57avbbo40y5tbppf7` (`google_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-26  9:53:55
