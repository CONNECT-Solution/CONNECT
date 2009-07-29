-- MySQL dump 10.11
--
-- Host: localhost    Database: patientcorrelationdb
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
-- Table structure for table `correlatedidentifiers`
--

DROP TABLE IF EXISTS `correlatedidentifiers`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `correlatedidentifiers` (
  `correlationId` int(10) unsigned NOT NULL auto_increment,
  `PatientAssigningAuthorityId` varchar(45) NOT NULL,
  `PatientId` varchar(45) NOT NULL,
  `CorrelatedPatientAssigningAuthorityId` varchar(45) NOT NULL,
  `CorrelatedPatientId` varchar(45) NOT NULL,
  PRIMARY KEY  (`correlationId`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `correlatedidentifiers`
--

LOCK TABLES `correlatedidentifiers` WRITE;
/*!40000 ALTER TABLE `correlatedidentifiers` DISABLE KEYS */;
INSERT INTO `correlatedidentifiers` VALUES (50,'2.16.840.1.113883.3.198.1.1','D123401','2.16.840.1.113883.3.200.1.1','500000000'),(51,'2.16.840.1.113883.3.198.1.1','D123403','2.16.840.1.113883.3.200.1.1','500000002'),(54,'2.16.840.1.113883.4.1','123456789','2.16.840.1.113883.3.198','D123401'),(55,'2.16.840.1.113883.3.200','500000000','2.16.840.1.113883.3.192','1018'),(56,'2.16.840.1.113883.3.192','1018','2.16.840.1.113883.3.200','500000000'),(57,'2.16.840.1.113883.3.200','500000000','2.16.840.1.113883.4.1','123456789'),(58,'2.16.840.1.113883.4.1','123456789','2.16.840.1.113883.3.200','500000000'),(59,'2.16.840.1.113883.3.198.1.1','D123411','2.16.840.1.113883.3.200.1.1','500000009');
/*!40000 ALTER TABLE `correlatedidentifiers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2009-02-05 14:56:14
