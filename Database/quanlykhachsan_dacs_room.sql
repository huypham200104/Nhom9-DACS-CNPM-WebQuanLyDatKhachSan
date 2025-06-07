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
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES ('3669d28d-eae6-4156-b6f7-49aaac858901','DOUBLE','2025-05-26','Phòng phổ thông',2,150000,8,'AVAILABLE',NULL,'2025-05-26','77d8a88b-3ca8-429d-a86b-35f168884439','2f3b2e8d-5c1d-4a8c-8a9e-6c4ef31f5c5a'),('743310d7-816b-47fb-a92e-d84690cb226a','SINGLE','2025-06-06','Phòng phổ thông',3,120000,12,'AVAILABLE',NULL,'2025-06-06','dc7ecb2a-0a9a-4d88-bb6c-5cbd5ca158f1','2f3b2e8d-5c1d-4a8c-8a9e-6c4ef31f5c5a'),('9d14bea7-9a95-425b-a103-2a8f5dad866c','KING','2025-06-07','Phòng VIP',1,120000,12,'AVAILABLE',NULL,'2025-06-07','a7f73fab-c900-4a84-92c1-d2c958df24ae','1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b'),('a25dafc0-2d11-453b-96fc-b1c16b4d4749','KING','2025-05-27','Phòng VIP',2,1500000,20,'AVAILABLE',NULL,'2025-05-27','2279a26a-8b58-49a0-984f-8026e746a60f','1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b'),('b0507210-445a-4b81-a342-99ba40b3e03d','KING','2025-06-06','Phòng VIP',2,520000,20,'AVAILABLE',NULL,'2025-06-06','dc7ecb2a-0a9a-4d88-bb6c-5cbd5ca158f1','1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b'),('c56d5d31-eb6b-480c-abef-8329a37d4b81','KING','2025-05-27','Phòng VIP',2,50000,20,'AVAILABLE',NULL,'2025-05-27','77d8a88b-3ca8-429d-a86b-35f168884439','1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b'),('e5a0e75d-29ff-4b8f-be6c-6aeb1ff5f792','SINGLE','2025-06-05','Phòng VIP',2,520000,20,'AVAILABLE',NULL,'2025-06-05','a7f73fab-c900-4a84-92c1-d2c958df24ae','1e9a2e7f-4b0c-4c87-8d6f-5c3ef31f5c4b');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `check_hotel_approved_before_insert_room` BEFORE INSERT ON `room` FOR EACH ROW BEGIN
    DECLARE hotel_status ENUM('APPROVED','PENDING','REJECTED');

    -- Lấy approval_status của khách sạn
    SELECT approval_status INTO hotel_status
    FROM hotel
    WHERE hotel_id = NEW.hotel_id;

    -- Nếu khách sạn không được duyệt thì không cho thêm phòng
    IF hotel_status != 'APPROVED' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot add room: Hotel is not approved.';
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
