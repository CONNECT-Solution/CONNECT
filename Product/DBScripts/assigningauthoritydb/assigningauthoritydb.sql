-- MySQL dump 10.11
--
-- Host: localhost    Database: assigningauthoritydb
-- ------------------------------------------------------
-- Server version	5.0.51b-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assigningauthoritytohomecommunitymapping`
--

DROP TABLE IF EXISTS `assigningauthoritytohomecommunitymapping`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `assigningauthoritytohomecommunitymapping` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `assigningauthorityid` varchar(45) NOT NULL,
  `homecommunityid` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`,`assigningauthorityid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `assigningauthoritytohomecommunitymapping`
--

LOCK TABLES `assigningauthoritytohomecommunitymapping` WRITE;
/*!40000 ALTER TABLE `assigningauthoritytohomecommunitymapping` DISABLE KEYS */;
INSERT INTO `assigningauthoritytohomecommunitymapping` VALUES (1,'1.1','1.2.3.4.55.500'),(2,'1.2','1.2.3.4.55.501'),(3,'1.3','2.16.840.1.113883.3.200'),(4,'1.4','2.16.840.1.113883.3.198'),(5,'1.5','2.16.840.1.113883.3.184');
/*!40000 ALTER TABLE `assigningauthoritytohomecommunitymapping` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2009-02-18 18:09:58
