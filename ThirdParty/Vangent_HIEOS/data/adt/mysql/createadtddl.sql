--
--  This code is subject to the HIEOS License, Version 1.0
-- 
--  Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
-- 
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- 
--  See the License for the specific language governing permissions and
--  limitations under the License.
--

-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.33-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema adt
--

CREATE DATABASE IF NOT EXISTS adt;
USE adt;

--
-- Definition of table `patient`
--

DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient` (
  `uuid` varchar(64) NOT NULL,
  `id` text NOT NULL,
  `timestamp` text,
  `birthdatetime` text,
  `adminsex` text,
  `accountnumber` text,
  `bedid` text,
  PRIMARY KEY (`id`(100)),
  KEY `patient_uuid_idx` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Dumping data for table `patient`
--

/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;


--
-- Definition of table `patientaddress`
--

DROP TABLE IF EXISTS `patientaddress`;
CREATE TABLE `patientaddress` (
  `parent` varchar(64) NOT NULL,
  `streetaddress` varchar(100) NOT NULL,
  `otherdesignation` varchar(64) NOT NULL,
  `city` varchar(32) NOT NULL,
  `stateorprovince` varchar(32) NOT NULL,
  `zipcode` varchar(12) NOT NULL,
  `country` varchar(32) NOT NULL,
  `countyorparish` varchar(32) NOT NULL,
  PRIMARY KEY (`parent`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `patientaddress`
--

/*!40000 ALTER TABLE `patientaddress` DISABLE KEYS */;
/*!40000 ALTER TABLE `patientaddress` ENABLE KEYS */;


--
-- Definition of table `patientname`
--

DROP TABLE IF EXISTS `patientname`;
CREATE TABLE `patientname` (
  `parent` varchar(64) NOT NULL,
  `familyname` varchar(32) NOT NULL,
  `givenname` varchar(32) NOT NULL,
  `secondandfurthername` varchar(32) NOT NULL,
  `suffix` varchar(12) NOT NULL,
  `prefix` varchar(12) NOT NULL,
  `degree` varchar(12) NOT NULL,
  PRIMARY KEY (`parent`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `patientname`
--

/*!40000 ALTER TABLE `patientname` DISABLE KEYS */;
/*!40000 ALTER TABLE `patientname` ENABLE KEYS */;


--
-- Definition of table `patientrace`
--

DROP TABLE IF EXISTS `patientrace`;
CREATE TABLE `patientrace` (
  `parent` varchar(64) NOT NULL,
  `race` varchar(64) NOT NULL,
  PRIMARY KEY (`parent`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `patientrace`
--

/*!40000 ALTER TABLE `patientrace` DISABLE KEYS */;
/*!40000 ALTER TABLE `patientrace` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
