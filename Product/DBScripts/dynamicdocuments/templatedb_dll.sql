CREATE DATABASE IF NOT EXISTS `templatedb` /*!40100 DEFAULT CHARACTER SET latin1 */;


DROP TABLE IF EXISTS `templatedb`.`section_module`;
DROP TABLE IF EXISTS `templatedb`.`doc_section`;
DROP TABLE IF EXISTS `templatedb`.`cda_template`;

CREATE TABLE  `templatedb`.`cda_template` (
  `ID` int(11) NOT NULL auto_increment,
  `TYPE` char(1) NOT NULL COMMENT 'D=Document, S=Section, M=Module',
  `DESCR` varchar(60) default NULL,
  `HITSP_TEMPLATE_ID` varchar(40) default NULL,
  `IHE_TEMPLATE_ID` varchar(40) default NULL,
  `LOINC_CODE` varchar(7) default NULL,
  `DATA_STATUS` char(1) default NULL COMMENT 'A=active only,I=inactive only,NULL=all',
  `DATA_DATE_RANGE_START` varchar(5) default NULL COMMENT 'Start date of data, same date concepts as CHCS where T=today, T-1=1 day from today. NULL=all data',
  `DATA_DATE_RANGE_END` varchar(5) default NULL COMMENT 'End date of data, same date concepts as CHCS where T=today, T-1=1 day from today. NULL=all data',
  `CDA_TEMPLATE_ID` varchar(40) default NULL,
  `HITSP_TEMPLATE_ID_PRE25` varchar(40) default NULL,
  PRIMARY KEY  USING BTREE (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;


CREATE TABLE  `templatedb`.`doc_section` (
  `DOC_ID` int(11) NOT NULL,
  `SECTION_ID` int(11) NOT NULL,
  `ACTIVE_YN` char(1) NOT NULL COMMENT 'Y=available, N=not available',
  PRIMARY KEY  (`DOC_ID`,`SECTION_ID`),
  KEY `SECTION_ID` (`SECTION_ID`),
  CONSTRAINT `doc_section_ibfk_1` FOREIGN KEY (`DOC_ID`) REFERENCES `cda_template` (`ID`),
  CONSTRAINT `doc_section_ibfk_2` FOREIGN KEY (`SECTION_ID`) REFERENCES `cda_template` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE  `templatedb`.`section_module` (
  `SECTION_ID` int(11) NOT NULL,
  `MODULE_ID` int(11) NOT NULL,
  `ACTIVE_YN` char(1) NOT NULL COMMENT 'Y=available, N=not available',
  PRIMARY KEY  (`SECTION_ID`,`MODULE_ID`),
  KEY `MODULE_ID` (`MODULE_ID`),
  CONSTRAINT `section_module_ibfk_1` FOREIGN KEY (`SECTION_ID`) REFERENCES `cda_template` (`ID`),
  CONSTRAINT `section_module_ibfk_2` FOREIGN KEY (`MODULE_ID`) REFERENCES `cda_template` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

GRANT SELECT,INSERT,UPDATE,DELETE ON templatedb.* to nhincuser;

-- end templatedb creation

-- populate cda_template table

insert into templatedb.cda_template values(1,'M','LANGUAGE SPOKEN','2.16.840.1.113883.3.88.11.83.2','1.3.6.1.4.1.19376.1.5.3.1.2.1','','','','','','2.16.840.1.113883.3.88.11.32.2');
insert into templatedb.cda_template values(2,'M','SUPPORT','2.16.840.1.113883.3.88.11.83.3','1.3.6.1.4.1.19376.1.5.3.1.2.4','','','','','','2.16.840.1.113883.3.88.11.32.3');
insert into templatedb.cda_template values(3,'M','HEALTHCARE PROVIDER','2.16.840.1.113883.3.88.11.83.4','1.3.6.1.4.1.19376.1.5.3.1.2.3','','','','','','2.16.840.1.113883.3.88.11.32.4');
insert into templatedb.cda_template values(4,'M','INSURANCE PROVIDER','2.16.840.1.113883.3.88.11.83.5','1.3.6.1.4.1.19376.1.5.3.1.4.17','','','','','','2.16.840.1.113883.3.88.11.32.5');
insert into templatedb.cda_template values(5,'M','ALLERGY/DRUG SENSITIVITY','2.16.840.1.113883.3.88.11.83.6','1.3.6.1.4.1.19376.1.5.3.1.4.5.3','','','','','2.16.840.1.113883.10.20.1.27','2.16.840.1.113883.3.88.11.32.6');
insert into templatedb.cda_template values(6,'M','CONDITION','2.16.840.1.113883.3.88.11.83.7','1.3.6.1.4.1.19376.1.5.3.1.4.5.2','','','','','2.16.840.1.113883.10.20.1.27','2.16.840.1.113883.3.88.11.32.7');
insert into templatedb.cda_template values(7,'M','MEDICATION','2.16.840.1.113883.3.88.11.83.8','1.3.6.1.4.1.19376.1.5.3.1.4.7','','A','','','2.16.840.1.113883.10.20.1.24','2.16.840.1.113883.3.88.11.32.8');
insert into templatedb.cda_template values(8,'M','COMMENT','2.16.840.1.113883.3.88.11.83.11','1.3.6.1.4.1.19376.1.5.3.1.4.2','','','','','2.16.840.1.113883.10.20.1.40','2.16.840.1.113883.3.88.11.32.12');
insert into templatedb.cda_template values(9,'M','ADVANCE DIRECTIVE','2.16.840.1.113883.3.88.11.83.12','1.3.6.1.4.1.19376.1.5.3.1.4.13.7','','','','','2.16.840.1.113883.10.20.1.17','2.16.840.1.113883.3.88.11.32.13');
insert into templatedb.cda_template values(10,'M','IMMUNIZATION','2.16.840.1.113883.3.88.11.83.13','1.3.6.1.4.1.19376.1.5.3.1.4.12','','','','','2.16.840.1.113883.10.20.1.24','2.16.840.1.113883.3.88.11.32.14');
insert into templatedb.cda_template values(11,'M','VITAL SIGN','2.16.840.1.113883.3.88.11.83.14','1.3.6.1.4.1.19376.1.5.3.1.4.13.1','','','','','','2.16.840.1.113883.3.88.11.32.15');
insert into templatedb.cda_template values(12,'M','RESULT','2.16.840.1.113883.3.88.11.83.15','1.3.6.1.4.1.19376.1.5.3.1.4.13','','','','','','2.16.840.1.113883.3.88.11.32.16');
insert into templatedb.cda_template values(13,'M','ENCOUNTER','2.16.840.1.113883.3.88.11.83.16','1.3.6.1.4.1.19376.1.5.3.1.4.14','','','','','','2.16.840.1.113883.3.88.11.32.17');
insert into templatedb.cda_template values(14,'M','PROCEDURE','2.16.840.1.113883.3.88.11.83.17','','','','','','','');
insert into templatedb.cda_template values(15,'M','FAMILY HISTORY','2.16.840.1.113883.3.88.11.83.18','','','','','','','');
insert into templatedb.cda_template values(16,'M','SOCIAL HISTORY','2.16.840.1.113883.3.88.11.83.19','','','','','','','');
insert into templatedb.cda_template values(21,'S','PAYERS SECTION','2.16.840.1.113883.3.88.11.83.101','1.3.6.1.4.1.19376.1.5.3.1.1.5.3.7','','','','','2.16.840.1.113883.10.20.1.9','2.16.840.1.113883.10.20.1.9');
insert into templatedb.cda_template values(22,'S','ALLERGIES AND OTHER ADVERSE REACTIONS SECTION','2.16.840.1.113883.3.88.11.83.102','1.3.6.1.4.1.19376.1.5.3.1.3.13','','A','','','2.16.840.1.113883.10.20.1.2','2.16.840.1.113883.10.20.1.2');
insert into templatedb.cda_template values(23,'S','PROBLEM LIST SECTION','2.16.840.1.113883.3.88.11.83.103','1.3.6.1.4.1.19376.1.5.3.1.3.6','','','','','2.16.840.1.113883.10.20.1.11','2.16.840.1.113883.10.20.1.11');
insert into templatedb.cda_template values(24,'S','HISTORY OF PAST ILLNESS SECTION','2.16.840.1.113883.3.88.11.83.104','1.3.6.1.4.1.19376.1.5.3.1.3.8','','','','','','');
insert into templatedb.cda_template values(25,'S','CHIEF COMPLAINT SECTION','2.16.840.1.113883.3.88.11.83.105','1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1','','','','','','');
insert into templatedb.cda_template values(26,'S','REASON FOR REFERRAL SECTION','2.16.840.1.113883.3.88.11.83.106','1.3.6.1.4.1.19376.1.5.3.1.3.1','','','','','','');
insert into templatedb.cda_template values(27,'S','HISTORY OF PRESENT ILLNESS SECTION','2.16.840.1.113883.3.88.11.83.107','1.3.6.1.4.1.19376.1.5.3.1.3.4','','','','','','');
insert into templatedb.cda_template values(28,'S','LIST OF SURGERIES SECTION','2.16.840.1.113883.3.88.11.83.108','1.3.6.1.4.1.19376.1.5.3.1.3.12','','','','','','');
insert into templatedb.cda_template values(29,'S','FUNCTIONAL STATUS SECTION','2.16.840.1.113883.3.88.11.83.109','','','','','','','');
insert into templatedb.cda_template values(30,'S','HOSPITAL ADMISSION DIAGNOSIS SECTION','2.16.840.1.113883.3.88.11.83.110','1.3.6.1.4.1.19376.1.5.3.1.3.3','','','','','','');
insert into templatedb.cda_template values(31,'S','DISCHARGE DIAGNOSIS SECTION','2.16.840.1.113883.3.88.11.83.111','1.3.6.1.4.1.19376.1.5.3.1.3.7','','','','','','');
insert into templatedb.cda_template values(32,'S','MEDICATIONS SECTION','2.16.840.1.113883.3.88.11.83.112','1.3.6.1.4.1.19376.1.5.3.1.3.19','','A','T-450','T','2.16.840.1.113883.10.20.1.8','2.16.840.1.113883.10.20.1.8');
insert into templatedb.cda_template values(33,'S','ADMISSION MEDICATIONS HISTORY SECTION','2.16.840.1.113883.3.88.11.83.113','1.3.6.1.4.1.19376.1.5.3.1.3.20','','','','','','');
insert into templatedb.cda_template values(34,'S','HOSPITAL DISCHARGE MEDICATIONS SECTION','2.16.840.1.113883.3.88.11.83.114','1.3.6.1.4.1.19376.1.5.3.1.3.22','','','','','','');
insert into templatedb.cda_template values(35,'S','MEDICATIONS ADMINISTERED SECTION','2.16.840.1.113883.3.88.11.83.115','1.3.6.1.4.1.19376.1.5.3.1.3.21','','','','','','');
insert into templatedb.cda_template values(36,'S','ADVANCED DIRECTIVES SECTION','2.16.840.1.113883.3.88.11.83.116','1.3.6.1.4.1.19376.1.5.3.1.3.35','','','','','2.16.840.1.113883.10.20.1.1','2.16.840.1.113883.10.20.1.1');
insert into templatedb.cda_template values(37,'S','IMMUNIZATIONS SECTION','2.16.840.1.113883.3.88.11.83.117','1.3.6.1.4.1.19376.1.5.3.1.3.23','','','','','2.16.840.1.113883.10.20.1.6','2.16.840.1.113883.10.20.1.6');
insert into templatedb.cda_template values(38,'S','PHYSICAL EXAM SECTION','2.16.840.1.113883.3.88.11.83.118','1.3.6.1.4.1.19376.1.5.3.1.1.9.15','','','','','','');
insert into templatedb.cda_template values(39,'S','VITAL SIGNS SECTION','2.16.840.1.113883.3.88.11.83.119','1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2','','','','','2.16.840.1.113883.10.20.1.16','2.16.840.1.113883.10.20.1.16');
insert into templatedb.cda_template values(40,'S','REVIEW OF SYSTEMS','2.16.840.1.113883.3.88.11.83.120','1.3.6.1.4.1.19376.1.5.3.1.3.18','','','','','','');
insert into templatedb.cda_template values(41,'S','HOSPITAL COURSE SECTION','2.16.840.1.113883.3.88.11.83.121','1.3.6.1.4.1.19376.1.5.3.1.3.5','','','','','','');
insert into templatedb.cda_template values(42,'S','DIAGNOSTIC RESULTS SECTION','2.16.840.1.113883.3.88.11.83.122','1.3.6.1.4.1.19376.1.5.3.1.3.28','','','','','2.16.840.1.113883.10.20.1.14','2.16.840.1.113883.10.20.1.14');
insert into templatedb.cda_template values(43,'S','ASSESSMENT & PLAN SECTION','2.16.840.1.113883.3.88.11.83.123','1.3.6.1.4.1.19376.1.5.3.1.1.13.2.5','','','','','','');
insert into templatedb.cda_template values(44,'S','PLAN OF CARE SECTION','2.16.840.1.113883.3.88.11.83.124','1.3.6.1.4.1.19376.1.5.3.1.3.31','','','','','2.16.840.1.113883.10.20.1.10','');
insert into templatedb.cda_template values(45,'S','FAMILY HISTORY SECTION','2.16.840.1.113883.3.88.11.83.125','1.3.6.1.4.1.19376.1.5.3.1.3.15','','','','','2.16.840.1.113883.10.20.1.4','');
insert into templatedb.cda_template values(46,'S','SOCIAL HISTORY SECTION','2.16.840.1.113883.3.88.11.83.126','1.3.6.1.4.1.19376.1.5.3.1.3.16','','','','','2.16.840.1.113883.10.20.1.15','');
insert into templatedb.cda_template values(47,'S','ENCOUNTERS SECTION','2.16.840.1.113883.3.88.11.83.127','1.3.6.1.4.1.19376.1.5.3.1.1.5.3.3','','','','','2.16.840.1.113883.10.20.1.3','2.16.840.1.113883.10.20.1.3');
insert into templatedb.cda_template values(48,'S','MEDICAL EQUIPMENT SECTION','2.16.840.1.113883.3.88.11.83.128','1.3.6.1.4.1.19376.1.5.3.1.1.5.3.5','','','','','2.16.840.1.113883.10.20.1.7','');
insert into templatedb.cda_template values(51,'D','HITSP/C32','2.16.840.1.113883.3.88.11.32.1','','34133-9','','','','','');
insert into templatedb.cda_template values(52,'D','HITSP/C28','2.16.840.1.113883.3.88.11.28','','','','','','','');
insert into templatedb.cda_template values(53,'D','HITSP/C37','2.16.840.1.113883.3.88.11.37','','','','','','','');
insert into templatedb.cda_template values(54,'D','HITSP/C48 REFERRAL SUMMARY','2.16.840.1.113883.3.88.11.48.1','','','','','','','');

-- populate doc_section table

insert into templatedb.doc_section values(51,22,'Y');
insert into templatedb.doc_section values(51,23,'Y');
insert into templatedb.doc_section values(51,32,'Y');

-- populate section_module table

insert into templatedb.section_module values(22,5,'Y');
insert into templatedb.section_module values(23,6,'Y');
insert into templatedb.section_module values(32,7,'Y');

commit;
