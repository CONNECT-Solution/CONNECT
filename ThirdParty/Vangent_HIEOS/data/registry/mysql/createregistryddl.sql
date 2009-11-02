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
-- Server version	5.1.34-community-log


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO,MYSQL323' */;


--
-- Create schema omar
--

CREATE DATABASE IF NOT EXISTS omar;
USE omar;

--
-- Temporary table structure for view `identifiable`
--
DROP TABLE IF EXISTS `identifiable`;
DROP VIEW IF EXISTS `identifiable`;
CREATE TABLE `identifiable` (
  `id` varchar(256),
  `home` varchar(256)
);

--
-- Definition of table `adhocquery`
--

DROP TABLE IF EXISTS `adhocquery`;
CREATE TABLE `adhocquery` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `queryLanguage` varchar(256) NOT NULL,
  `query` varchar(4096) NOT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `adhocquery`
--

/*!40000 ALTER TABLE `adhocquery` DISABLE KEYS */;
/*!40000 ALTER TABLE `adhocquery` ENABLE KEYS */;


--
-- Definition of table `affectedobject`
--

DROP TABLE IF EXISTS `affectedobject`;
CREATE TABLE `affectedobject` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `eventId` varchar(256) NOT NULL,
  PRIMARY KEY (`id`,`eventId`),
  KEY `id_AFOBJ_idx` (`id`),
  KEY `evid_AFOBJ_idx` (`eventId`)
) TYPE=InnoDB;

--
-- Dumping data for table `affectedobject`
--

/*!40000 ALTER TABLE `affectedobject` DISABLE KEYS */;
/*!40000 ALTER TABLE `affectedobject` ENABLE KEYS */;


--
-- Definition of table `association`
--

DROP TABLE IF EXISTS `association`;
CREATE TABLE `association` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `associationType` varchar(256) NOT NULL,
  `sourceObject` varchar(256) NOT NULL,
  `targetObject` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `src_Ass_idx` (`sourceObject`),
  KEY `tgt_Ass_idx` (`targetObject`),
  KEY `type_Ass_idx` (`associationType`)
) TYPE=InnoDB;

--
-- Dumping data for table `association`
--

/*!40000 ALTER TABLE `association` DISABLE KEYS */;
/*!40000 ALTER TABLE `association` ENABLE KEYS */;


--
-- Definition of table `auditableevent`
--

DROP TABLE IF EXISTS `auditableevent`;
CREATE TABLE `auditableevent` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `requestId` varchar(256) NOT NULL,
  `eventType` varchar(256) NOT NULL,
  `timeStamp_` varchar(30) NOT NULL,
  `user_` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `lid_AUEVENT_evtTyp` (`eventType`)
) TYPE=InnoDB;

--
-- Dumping data for table `auditableevent`
--

/*!40000 ALTER TABLE `auditableevent` DISABLE KEYS */;
/*!40000 ALTER TABLE `auditableevent` ENABLE KEYS */;


--
-- Definition of table `classification`
--

DROP TABLE IF EXISTS `classification`;
CREATE TABLE `classification` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `classificationNode` varchar(256) DEFAULT NULL,
  `classificationScheme` varchar(256) DEFAULT NULL,
  `classifiedObject` varchar(256) NOT NULL,
  `nodeRepresentation` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `clsObj_Class_idx` (`classifiedObject`)
) TYPE=InnoDB;

--
-- Dumping data for table `classification`
--

/*!40000 ALTER TABLE `classification` DISABLE KEYS */;
/*!40000 ALTER TABLE `classification` ENABLE KEYS */;


--
-- Definition of table `classificationnode`
--

DROP TABLE IF EXISTS `classificationnode`;
CREATE TABLE `classificationnode` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `code` varchar(256) DEFAULT NULL,
  `parent` varchar(256) DEFAULT NULL,
  `path` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_Node_idx` (`parent`),
  KEY `code_Node_idx` (`code`),
  KEY `path_Node_idx` (`path`(767))
) TYPE=InnoDB;

--
-- Dumping data for table `classificationnode`
--

/*!40000 ALTER TABLE `classificationnode` DISABLE KEYS */;
/*!40000 ALTER TABLE `classificationnode` ENABLE KEYS */;


--
-- Definition of table `classscheme`
--

DROP TABLE IF EXISTS `classscheme`;
CREATE TABLE `classscheme` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `isInternal` varchar(1) NOT NULL,
  `nodeType` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `classscheme`
--

/*!40000 ALTER TABLE `classscheme` DISABLE KEYS */;
/*!40000 ALTER TABLE `classscheme` ENABLE KEYS */;


--
-- Definition of table `description`
--

DROP TABLE IF EXISTS `description`;
CREATE TABLE `description` (
  `charset` varchar(32) DEFAULT NULL,
  `lang` varchar(32) NOT NULL,
  `value` varchar(1024) NOT NULL,
  `parent` varchar(256) NOT NULL,
  PRIMARY KEY (`parent`)
) TYPE=InnoDB;

--
-- Dumping data for table `description`
--

/*!40000 ALTER TABLE `description` DISABLE KEYS */;
/*!40000 ALTER TABLE `description` ENABLE KEYS */;


--
-- Definition of table `emailaddress`
--

DROP TABLE IF EXISTS `emailaddress`;
CREATE TABLE `emailaddress` (
  `address` varchar(64) NOT NULL,
  `type` varchar(256) DEFAULT NULL,
  `parent` varchar(256) NOT NULL,
  KEY `parent_EmlAdr_idx` (`parent`)
) TYPE=InnoDB;

--
-- Dumping data for table `emailaddress`
--

/*!40000 ALTER TABLE `emailaddress` DISABLE KEYS */;
/*!40000 ALTER TABLE `emailaddress` ENABLE KEYS */;


--
-- Definition of table `externalidentifier`
--

DROP TABLE IF EXISTS `externalidentifier`;
CREATE TABLE `externalidentifier` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `registryObject` varchar(256) NOT NULL,
  `identificationScheme` varchar(256) NOT NULL,
  `value` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ro_EID_idx` (`registryObject`),
  KEY `idscheme_EID_idx` (`identificationScheme`),
  KEY `value_EID_idx` (`value`)
) TYPE=InnoDB;

--
-- Dumping data for table `externalidentifier`
--

/*!40000 ALTER TABLE `externalidentifier` DISABLE KEYS */;
/*!40000 ALTER TABLE `externalidentifier` ENABLE KEYS */;


--
-- Definition of table `externallink`
--

DROP TABLE IF EXISTS `externallink`;
CREATE TABLE `externallink` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `externalURI` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uri_ExLink_idx` (`externalURI`)
) TYPE=InnoDB;

--
-- Dumping data for table `externallink`
--

/*!40000 ALTER TABLE `externallink` DISABLE KEYS */;
/*!40000 ALTER TABLE `externallink` ENABLE KEYS */;


--
-- Definition of table `extrinsicobject`
--

DROP TABLE IF EXISTS `extrinsicobject`;
CREATE TABLE `extrinsicobject` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `isOpaque` varchar(1) NOT NULL,
  `mimeType` varchar(256) DEFAULT NULL,
  `contentVersionName` varchar(16) DEFAULT NULL,
  `contentVersionComment` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `extrinsicobject`
--

/*!40000 ALTER TABLE `extrinsicobject` DISABLE KEYS */;
/*!40000 ALTER TABLE `extrinsicobject` ENABLE KEYS */;


--
-- Definition of table `federation`
--

DROP TABLE IF EXISTS `federation`;
CREATE TABLE `federation` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `replicationSyncLatency` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `federation`
--

/*!40000 ALTER TABLE `federation` DISABLE KEYS */;
/*!40000 ALTER TABLE `federation` ENABLE KEYS */;


--
-- Definition of table `name_`
--

DROP TABLE IF EXISTS `name_`;
CREATE TABLE `name_` (
  `charset` varchar(32) DEFAULT NULL,
  `lang` varchar(32) NOT NULL,
  `value` varchar(1024) NOT NULL,
  `parent` varchar(256) NOT NULL,
  PRIMARY KEY (`parent`)
) TYPE=InnoDB;

--
-- Dumping data for table `name_`
--

/*!40000 ALTER TABLE `name_` DISABLE KEYS */;
/*!40000 ALTER TABLE `name_` ENABLE KEYS */;


--
-- Definition of table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `subscription` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `notification`
--

/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;


--
-- Definition of table `notificationobject`
--

DROP TABLE IF EXISTS `notificationobject`;
CREATE TABLE `notificationobject` (
  `notificationId` varchar(256) NOT NULL,
  `registryObjectId` varchar(256) NOT NULL,
  PRIMARY KEY (`notificationId`,`registryObjectId`)
) TYPE=InnoDB;

--
-- Dumping data for table `notificationobject`
--

/*!40000 ALTER TABLE `notificationobject` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificationobject` ENABLE KEYS */;


--
-- Definition of table `notifyaction`
--

DROP TABLE IF EXISTS `notifyaction`;
CREATE TABLE `notifyaction` (
  `notificationOption` varchar(256) NOT NULL,
  `endPoint` varchar(256) NOT NULL,
  `parent` varchar(256) NOT NULL
) TYPE=InnoDB;

--
-- Dumping data for table `notifyaction`
--

/*!40000 ALTER TABLE `notifyaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifyaction` ENABLE KEYS */;


--
-- Definition of table `objectref`
--

DROP TABLE IF EXISTS `objectref`;
CREATE TABLE `objectref` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `objectref`
--

/*!40000 ALTER TABLE `objectref` DISABLE KEYS */;
/*!40000 ALTER TABLE `objectref` ENABLE KEYS */;


--
-- Definition of table `organization`
--

DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `parent` varchar(256) DEFAULT NULL,
  `primaryContact` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_ORG_idx` (`parent`)
) TYPE=InnoDB;

--
-- Dumping data for table `organization`
--

/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;


--
-- Definition of table `person`
--

DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `personName_firstName` varchar(64) DEFAULT NULL,
  `personName_middleName` varchar(64) DEFAULT NULL,
  `personName_lastName` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lastNm_Person_idx` (`personName_lastName`)
) TYPE=InnoDB;

--
-- Dumping data for table `person`
--

/*!40000 ALTER TABLE `person` DISABLE KEYS */;
/*!40000 ALTER TABLE `person` ENABLE KEYS */;


--
-- Definition of table `postaladdress`
--

DROP TABLE IF EXISTS `postaladdress`;
CREATE TABLE `postaladdress` (
  `city` varchar(64) DEFAULT NULL,
  `country` varchar(64) DEFAULT NULL,
  `postalCode` varchar(64) DEFAULT NULL,
  `state` varchar(64) DEFAULT NULL,
  `street` varchar(64) DEFAULT NULL,
  `streetNumber` varchar(32) DEFAULT NULL,
  `parent` varchar(256) NOT NULL,
  KEY `parent_PstlAdr_idx` (`parent`),
  KEY `city_PstlAdr_idx` (`city`),
  KEY `cntry_PstlAdr_idx` (`country`),
  KEY `pCode_PstlAdr_idx` (`postalCode`)
) TYPE=InnoDB;

--
-- Dumping data for table `postaladdress`
--

/*!40000 ALTER TABLE `postaladdress` DISABLE KEYS */;
/*!40000 ALTER TABLE `postaladdress` ENABLE KEYS */;


--
-- Definition of table `registry`
--

DROP TABLE IF EXISTS `registry`;
CREATE TABLE `registry` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `catalogingSyncLatency` varchar(32) DEFAULT 'P1D',
  `conformanceProfile` varchar(16) DEFAULT NULL,
  `operator` varchar(256) NOT NULL,
  `replicationSyncLatency` varchar(32) DEFAULT 'P1D',
  `specificationVersion` varchar(8) NOT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `registry`
--

/*!40000 ALTER TABLE `registry` DISABLE KEYS */;
/*!40000 ALTER TABLE `registry` ENABLE KEYS */;


--
-- Definition of table `registryobject`
--

DROP TABLE IF EXISTS `registryobject`;
CREATE TABLE `registryobject` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) NOT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`,`objectType`)
) TYPE=InnoDB;

--
-- Dumping data for table `registryobject`
--

/*!40000 ALTER TABLE `registryobject` DISABLE KEYS */;
/*!40000 ALTER TABLE `registryobject` ENABLE KEYS */;


--
-- Definition of table `registrypackage`
--

DROP TABLE IF EXISTS `registrypackage`;
CREATE TABLE `registrypackage` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `registrypackage`
--

/*!40000 ALTER TABLE `registrypackage` DISABLE KEYS */;
/*!40000 ALTER TABLE `registrypackage` ENABLE KEYS */;


--
-- Definition of table `repositoryitem`
--

DROP TABLE IF EXISTS `repositoryitem`;
CREATE TABLE `repositoryitem` (
  `lid` varchar(256) NOT NULL,
  `versionName` varchar(16) NOT NULL,
  `content` blob,
  PRIMARY KEY (`lid`,`versionName`)
) TYPE=InnoDB;

--
-- Dumping data for table `repositoryitem`
--

/*!40000 ALTER TABLE `repositoryitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `repositoryitem` ENABLE KEYS */;


--
-- Definition of table `service`
--

DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `service`
--

/*!40000 ALTER TABLE `service` DISABLE KEYS */;
/*!40000 ALTER TABLE `service` ENABLE KEYS */;


--
-- Definition of table `servicebinding`
--

DROP TABLE IF EXISTS `servicebinding`;
CREATE TABLE `servicebinding` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `service` varchar(256) NOT NULL,
  `accessURI` varchar(256) DEFAULT NULL,
  `targetBinding` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `service_BIND_idx` (`service`)
) TYPE=InnoDB;

--
-- Dumping data for table `servicebinding`
--

/*!40000 ALTER TABLE `servicebinding` DISABLE KEYS */;
/*!40000 ALTER TABLE `servicebinding` ENABLE KEYS */;


--
-- Definition of table `slot`
--

DROP TABLE IF EXISTS `slot`;
CREATE TABLE `slot` (
  `sequenceId` int(11) NOT NULL,
  `name_` varchar(256) NOT NULL,
  `slotType` varchar(256) DEFAULT NULL,
  `value` varchar(256) DEFAULT NULL,
  `parent` varchar(256) NOT NULL,
  PRIMARY KEY (`parent`,`name_`,`sequenceId`)
) TYPE=InnoDB;

--
-- Dumping data for table `slot`
--

/*!40000 ALTER TABLE `slot` DISABLE KEYS */;
/*!40000 ALTER TABLE `slot` ENABLE KEYS */;


--
-- Definition of table `specificationlink`
--

DROP TABLE IF EXISTS `specificationlink`;
CREATE TABLE `specificationlink` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `serviceBinding` varchar(256) NOT NULL,
  `specificationObject` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `binding_SLnk_idx` (`serviceBinding`),
  KEY `spec_SLnk_idx` (`specificationObject`)
) TYPE=InnoDB;

--
-- Dumping data for table `specificationlink`
--

/*!40000 ALTER TABLE `specificationlink` DISABLE KEYS */;
/*!40000 ALTER TABLE `specificationlink` ENABLE KEYS */;


--
-- Definition of table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
CREATE TABLE `subscription` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `selector` varchar(256) NOT NULL,
  `endTime` varchar(30) DEFAULT NULL,
  `notificationInterval` varchar(32) DEFAULT 'P1D',
  `startTime` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `subscription`
--

/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;


--
-- Definition of table `telephonenumber`
--

DROP TABLE IF EXISTS `telephonenumber`;
CREATE TABLE `telephonenumber` (
  `areaCode` varchar(8) DEFAULT NULL,
  `countryCode` varchar(8) DEFAULT NULL,
  `extension` varchar(8) DEFAULT NULL,
  `number_` varchar(16) DEFAULT NULL,
  `phoneType` varchar(256) DEFAULT NULL,
  `parent` varchar(256) NOT NULL,
  KEY `parent_Phone_idx` (`parent`)
) TYPE=InnoDB;

--
-- Dumping data for table `telephonenumber`
--

/*!40000 ALTER TABLE `telephonenumber` DISABLE KEYS */;
/*!40000 ALTER TABLE `telephonenumber` ENABLE KEYS */;


--
-- Definition of table `usagedescription`
--

DROP TABLE IF EXISTS `usagedescription`;
CREATE TABLE `usagedescription` (
  `charset` varchar(32) DEFAULT NULL,
  `lang` varchar(32) NOT NULL,
  `value` varchar(1024) NOT NULL,
  `parent` varchar(256) NOT NULL,
  PRIMARY KEY (`parent`,`lang`),
  KEY `value_UsgDes_idx` (`value`(767))
) TYPE=InnoDB;

--
-- Dumping data for table `usagedescription`
--

/*!40000 ALTER TABLE `usagedescription` DISABLE KEYS */;
/*!40000 ALTER TABLE `usagedescription` ENABLE KEYS */;


--
-- Definition of table `usageparameter`
--

DROP TABLE IF EXISTS `usageparameter`;
CREATE TABLE `usageparameter` (
  `value` varchar(1024) NOT NULL,
  `parent` varchar(256) NOT NULL
) TYPE=InnoDB;

--
-- Dumping data for table `usageparameter`
--

/*!40000 ALTER TABLE `usageparameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `usageparameter` ENABLE KEYS */;


--
-- Definition of table `user_`
--

DROP TABLE IF EXISTS `user_`;
CREATE TABLE `user_` (
  `id` varchar(256) NOT NULL,
  `home` varchar(256) DEFAULT NULL,
  `lid` varchar(256) NOT NULL,
  `objectType` varchar(256) DEFAULT NULL,
  `status` varchar(256) NOT NULL,
  `versionName` varchar(16) DEFAULT NULL,
  `comment_` varchar(256) DEFAULT NULL,
  `personName_firstName` varchar(64) DEFAULT NULL,
  `personName_middleName` varchar(64) DEFAULT NULL,
  `personName_lastName` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) TYPE=InnoDB;

--
-- Dumping data for table `user_`
--

/*!40000 ALTER TABLE `user_` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_` ENABLE KEYS */;


--
-- Definition of view `identifiable`
--

DROP TABLE IF EXISTS `identifiable`;
DROP VIEW IF EXISTS `identifiable`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `identifiable` AS select `adhocquery`.`id` AS `id`,`adhocquery`.`home` AS `home` from `adhocquery` union all select `association`.`id` AS `id`,`association`.`home` AS `home` from `association` union all select `auditableevent`.`id` AS `id`,`auditableevent`.`home` AS `home` from `auditableevent` union all select `classification`.`id` AS `id`,`classification`.`home` AS `home` from `classification` union all select `classificationnode`.`id` AS `id`,`classificationnode`.`home` AS `home` from `classificationnode` union all select `classscheme`.`id` AS `id`,`classscheme`.`home` AS `home` from `classscheme` union all select `externalidentifier`.`id` AS `id`,`externalidentifier`.`home` AS `home` from `externalidentifier` union all select `externallink`.`id` AS `id`,`externallink`.`home` AS `home` from `externallink` union all select `extrinsicobject`.`id` AS `id`,`extrinsicobject`.`home` AS `home` from `extrinsicobject` union all select `federation`.`id` AS `id`,`federation`.`home` AS `home` from `federation` union all select `organization`.`id` AS `id`,`organization`.`home` AS `home` from `organization` union all select `registry`.`id` AS `id`,`registry`.`home` AS `home` from `registry` union all select `registrypackage`.`id` AS `id`,`registrypackage`.`home` AS `home` from `registrypackage` union all select `service`.`id` AS `id`,`service`.`home` AS `home` from `service` union all select `servicebinding`.`id` AS `id`,`servicebinding`.`home` AS `home` from `servicebinding` union all select `specificationlink`.`id` AS `id`,`specificationlink`.`home` AS `home` from `specificationlink` union all select `subscription`.`id` AS `id`,`subscription`.`home` AS `home` from `subscription` union all select `user_`.`id` AS `id`,`user_`.`home` AS `home` from `user_` union all select `person`.`id` AS `id`,`person`.`home` AS `home` from `person` union all select `objectref`.`id` AS `id`,`objectref`.`home` AS `home` from `objectref`;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
