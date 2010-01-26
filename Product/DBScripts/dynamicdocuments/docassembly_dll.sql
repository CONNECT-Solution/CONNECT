CREATE DATABASE IF NOT EXISTS `docassembly` /*!40100 DEFAULT CHARACTER SET latin1 */;


DROP TABLE IF EXISTS `docassembly`.`das_config`;
DROP TABLE IF EXISTS `docassembly`.`document_type`;

CREATE TABLE  `docassembly`.`document_type` (
  `TYPE_ID` varchar(7) NOT NULL COMMENT 'Unique identifier for document type, i.e. "C32", "C37"',
  `DISPLAY_NAME` varchar(60) NOT NULL COMMENT 'Description fo document type',
  `ACTIVE` tinyint(1) NOT NULL COMMENT 'Indicate whether document type is available (true or 1) or unavailable (false or 0).',
  `VERSION` varchar(5) default NULL,
  `COMMENTS` varchar(100) default NULL,
  `CODE_SYSTEM_OID` varchar(30) default NULL,
  `CODE_SYSTEM` varchar(5) default NULL,
  PRIMARY KEY  USING BTREE (`TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE  `docassembly`.`das_config` (
  `ATT_NAME` varchar(50) NOT NULL,
  `ATT_VALUE` varchar(100) default NULL,
  `ACTIVE_YN` char(1) NOT NULL,
  PRIMARY KEY  (`ATT_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

GRANT SELECT,INSERT,UPDATE,DELETE ON docassembly.* to nhincuser;

-- end docassembly creation

-- populate document_type table

insert into docassembly.document_type values('10160-0','HISTORY OF MEDICATION USE',0,'2.0','Used for documents containing only a consumer''s medication history','','');
insert into docassembly.document_type values('11502-2','LABORATORY REPORT',0,'2.0','Used for Laboratory Reports','','');
insert into docassembly.document_type values('34133-9','SUMMARIZATION OF EPISODE NOTE',1,'2.1','C32 - Used for Summary Patient Record (CCD)','2.16.840.1.113883.6.1','');

-- populate das_config table

insert into docassembly.das_config values('das.demo', 'on', 'Y');
insert into docassembly.das_config values('das.demo.data.directory', '/nhin/das-demo-files', 'Y');
insert into docassembly.das_config values('das.demo.doc.date', '', 'Y');
insert into docassembly.das_config values('das.demo.patient', '', 'Y');
insert into docassembly.das_config values('das.persistenceUnit', 'documentassembleyPU', 'Y');
insert into docassembly.das_config values('das.dataservice.endpoint', 'http://localhost:8080/CommonDataLayerService/AdapterCommonDataLayer?wsdl', 'Y');
insert into docassembly.das_config values('das.docmgrservice.endpoint', 'http://localhost:8080/DocumentManager_Service/DocumentManagerService?wsdl', 'Y');
insert into docassembly.das_config values('das.terminologyservice.endpoint', 'http://localhost:8080/TerminologyService/TerminologyService?wsdl', 'Y');
insert into docassembly.das_config values('das.hitsp.pre25.templates','Y','Y');
insert into docassembly.das_config values('das.test', 'off', 'Y');
insert into docassembly.das_config values('das.version', '1.0', 'Y');
insert into docassembly.das_config values('DEF_CONFIDENTIAL_CODE', 'N', 'Y');
insert into docassembly.das_config values('DEF_CONFIDENTIAL_CODE_DESCR', 'Normal', 'Y');
insert into docassembly.das_config values('DEF_CONFIDENTIAL_CODE_SYS_OID', '2.16.840.1.113883.5.25', 'Y');
insert into docassembly.das_config values('DEF_FORMAT_CODE_DESCR', 'HL7 CCD Document', 'Y');
insert into docassembly.das_config values('DEF_FORMAT_CODE_OID', '2.16.840.1.1.113883.10.20.1', 'Y');
insert into docassembly.das_config values('DEF_LANGUAGE', 'EN-US', 'Y');
insert into docassembly.das_config values('HCFT_CODE', '2865M2000N', 'Y');
insert into docassembly.das_config values('HCFT_CODE_DESCR', 'Hospitals; Military; Medical Center', 'Y');
insert into docassembly.das_config values('HCFT_CODE_SYS_OID', '2.16.840.1.1.113883.5.111', 'Y');
insert into docassembly.das_config values('HL7_DELIMITER', '^', 'Y');
insert into docassembly.das_config values('ORGANIZATION_NAME', 'Department of Defense', 'Y');
insert into docassembly.das_config values('ORGANIZATION_OID', '2.16.840.1.113883.3.198', 'Y');
insert into docassembly.das_config values('ORGANIZATION_SYS', 'AHLTA', 'Y');
insert into docassembly.das_config values('ORGANIZATION_SYS_TYPE', 'Automated System', 'Y');
insert into docassembly.das_config values('PRACTICE_SETTING_CODE', '410001006', 'Y');
insert into docassembly.das_config values('PRACTICE_SETTING_CODE_DESCR', 'Military Medicine', 'Y');
insert into docassembly.das_config values('PRACTICE_SETTING_CODE_SYS_NAME', 'SNOMED-CT', 'Y');
insert into docassembly.das_config values('PRACTICE_SETTING_CODE_SYS_OID', '2.16.840.1.113883.6.96', 'Y');
insert into docassembly.das_config values('RXNORM_CODE_SYS_NAME', 'RXNorm', 'Y');
insert into docassembly.das_config values('RXNORM_CODE_SYS_OID', '2.16.840.1.113883.6.88', 'Y');
insert into docassembly.das_config values('SNOMED_CODE_SYS_NAME', 'SNOMED-CT', 'Y');
insert into docassembly.das_config values('SNOMED_CODE_SYS_OID', '2.16.840.1.113883.6.96', 'Y');
insert into docassembly.das_config values('ICD9_CODE_SYS_NAME', 'ICD9', 'Y');
insert into docassembly.das_config values('ICD9_CODE_SYS_OID', '2.16.840.1.113883.6.42', 'Y');
insert into docassembly.das_config values('NDC_CODE_SYS_NAME', 'NDC', 'Y');
insert into docassembly.das_config values('NDC_CODE_SYS_OID', '2.16.840.1.113883.6.69', 'Y');
insert into docassembly.das_config values('das.c32.stylesheet', 'CCD_DOD.xsl', 'Y');
commit;
