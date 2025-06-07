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
-- Dumping data for table `amenity`
--

LOCK TABLES `amenity` WRITE;
/*!40000 ALTER TABLE `amenity` DISABLE KEYS */;
INSERT INTO `amenity` VALUES (1,'Tivi 4K Smart TV','AVAILABLE','Màn hình 55 inch, kết nối Internet, hỗ trợ Netflix/Youtube'),(2,'Máy lạnh Inverter','AVAILABLE','Tiết kiệm điện năng, điều chỉnh nhiệt độ thông minh, hoạt động êm ái'),(3,'Bồn tắm Jacuzzi','AVAILABLE','Thư giãn với hệ thống sục khí, thiết kế sang trọng'),(4,'Wi-Fi miễn phí','AVAILABLE','Tốc độ cao, phủ sóng toàn bộ phòng nghỉ'),(5,'Két sắt an toàn','AVAILABLE','Hệ thống bảo mật hiện đại, có mã khóa'),(6,'Bàn làm việc','AVAILABLE','Không gian tiện nghi, có đèn bàn và ghế êm ái'),(7,'Điện thoại nội bộ','AVAILABLE','Liên lạc dễ dàng với lễ tân và các dịch vụ trong khách sạn'),(8,'Máy sấy tóc','AVAILABLE','Công suất cao, có nhiều chế độ điều chỉnh nhiệt độ'),(9,'Ghế massage','AVAILABLE','Chức năng thư giãn toàn thân, điều chỉnh theo nhu cầu'),(10,'Loa Bluetooth','UNAVAILABLE','Chất lượng âm thanh cao, kết nối nhanh với thiết bị di động'),(13,'Lò sưởi','AVAILABLE','Lò sưởi siêu ấm');
/*!40000 ALTER TABLE `amenity` ENABLE KEYS */;
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
