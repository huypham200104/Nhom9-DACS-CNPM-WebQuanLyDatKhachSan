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
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `booking_id` char(36) NOT NULL,
  `booking_status` enum('CANCELLED','CHECKED_IN','CHECKED_OUT','NO_SHOW','PAID','PENDING','REFUNDED') NOT NULL,
  `check_in_date` date NOT NULL,
  `check_out_date` date NOT NULL,
  `created_at` date DEFAULT NULL,
  `number_of_guests` int NOT NULL,
  `price_before_discount` bigint NOT NULL,
  `special_requests` varchar(2000) DEFAULT NULL,
  `total_price` bigint NOT NULL,
  `updated_at` date DEFAULT NULL,
  `customer_id` char(36) DEFAULT NULL,
  `discount_id` char(36) DEFAULT NULL,
  `room_id` char(36) DEFAULT NULL,
  `total_stays` int NOT NULL,
  PRIMARY KEY (`booking_id`),
  UNIQUE KEY `unique_customer_booking` (`customer_id`,`check_in_date`,`check_out_date`),
  KEY `FKg1g5k55pf72f5pftba35ark24` (`discount_id`),
  KEY `FKq83pan5xy2a6rn0qsl9bckqai` (`room_id`),
  CONSTRAINT `FKg1g5k55pf72f5pftba35ark24` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`discount_id`),
  CONSTRAINT `FKlnnelfsha11xmo2ndjq66fvro` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `FKq83pan5xy2a6rn0qsl9bckqai` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES ('6ed3fc19-d7fc-4d29-847d-8fec91250505','PAID','2025-06-07','2025-06-16','2025-06-07',1,1080000,'Mang thú cưng',1080000,'2025-06-07','e9bb1481-3cf5-4703-918f-18b79acd2353',NULL,NULL,9),('a71b62e2-2569-497e-9184-fd9004f21d34','PAID','2025-07-01','2025-07-03','2025-06-07',1,270000,NULL,300000,'2025-06-07','e9bb1481-3cf5-4703-918f-18b79acd2353',NULL,NULL,2),('b1b65e8f-09be-48eb-b42b-e4e79d546988','CHECKED_OUT','2025-06-07','2025-06-09','2025-06-05',1,50000,'',100000,'2025-06-06','e9bb1481-3cf5-4703-918f-18b79acd2353',NULL,NULL,2),('bdc4eb53-6695-4c86-86f5-b66918b232de','PAID','2025-07-11','2025-08-08','2025-06-07',1,3360000,NULL,4200000,'2025-06-07','e9bb1481-3cf5-4703-918f-18b79acd2353',NULL,NULL,28),('c0cf42da-9a53-4f80-b43f-c22ca820388a','PAID','2025-06-11','2025-06-12','2025-06-07',1,150000,'Mang thú cưng',150000,'2025-06-07','e9bb1481-3cf5-4703-918f-18b79acd2353',NULL,NULL,1);
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `prevent_overlapping_bookings` BEFORE INSERT ON `booking` FOR EACH ROW BEGIN
    DECLARE overlap_count INT;

    -- Kiểm tra xem có bản ghi nào trùng lặp cho cùng room_id không
    SELECT COUNT(*)
    INTO overlap_count
    FROM booking
    WHERE room_id = NEW.room_id
      AND booking_status NOT IN ('CANCELLED', 'REFUNDED') -- Bỏ qua các booking đã hủy hoặc hoàn tiền
      AND (
          -- Kiểm tra xem khoảng thời gian mới có chồng chéo với khoảng thời gian hiện có không
          (NEW.check_in_date <= check_out_date AND NEW.check_out_date >= check_in_date)
      );

    IF overlap_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Không thể đặt phòng: Phòng đã được đặt cho khoảng thời gian này.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-07 18:53:12
