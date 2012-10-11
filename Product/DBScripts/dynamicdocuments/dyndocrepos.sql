/* Create a new database - separate from the documents database for Dynamic Documents */
DROP DATABASE IF EXISTS dyndocrepos;

-- begin dyndocrepos
CREATE DATABASE IF NOT EXISTS dyndocrepos;

DROP TABLE IF EXISTS dyndocrepos.document;

CREATE TABLE IF NOT EXISTS dyndocrepos.document (
  documentid int(11) NOT NULL,
  DocumentUniqueId varchar(64) NOT NULL,
  DocumentTitle varchar(128) default NULL,
  authorPerson varchar(64) default NULL,
  authorInstitution varchar(64) default NULL,
  authorRole varchar(64) default NULL,
  authorSpecialty varchar(64) default NULL,
  AvailabilityStatus varchar(64) default NULL,
  ClassCode varchar(64) default NULL,
  ClassCodeScheme varchar(64) default NULL,
  ClassCodeDisplayName varchar(64) default NULL,
  ConfidentialityCode varchar(64) default NULL,
  ConfidentialityCodeScheme varchar(64) default NULL,
  ConfidentialityCodeDisplayName varchar(64) default NULL,
  CreationTime datetime default NULL COMMENT 'Date format expected: MM/dd/yyyy.HH:mm:ss',
  FormatCode varchar(64) default NULL,
  FormatCodeScheme varchar(64) default NULL,
  FormatCodeDisplayName varchar(64) default NULL,
  PatientId varchar(64) default NULL COMMENT 'Format of HL7 2.x CX',
  ServiceStartTime datetime default NULL COMMENT 'Format of YYYYMMDDHHMMSS',
  ServiceStopTime datetime default NULL COMMENT 'Format of YYYYMMDDHHMMSS',
  Status varchar(64) default NULL,
  Comments varchar(256) default NULL,
  Hash varchar(1028) default NULL COMMENT 'Might be better to derive',
  FacilityCode varchar(64) default NULL,
  FacilityCodeScheme varchar(64) default NULL,
  FacilityCodeDisplayName varchar(64) default NULL,
  IntendedRecipientPerson varchar(128) default NULL COMMENT 'Format of HL7 2.x XCN',
  IntendedRecipientOrganization varchar(128) default NULL COMMENT 'Format of HL7 2.x XON',
  LanguageCode varchar(64) default NULL,
  LegalAuthenticator varchar(128) default NULL COMMENT 'Format of HL7 2.x XCN',
  MimeType varchar(32) default NULL,
  ParentDocumentId varchar(64) default NULL,
  ParentDocumentRelationship varchar(64) default NULL,
  PracticeSetting varchar(64) default NULL,
  PracticeSettingScheme varchar(64) default NULL,
  PracticeSettingDisplayName varchar(64) default NULL,
  DocumentSize bigint(20) default NULL,
  SourcePatientId varchar(128) default NULL COMMENT 'Format of HL7 2.x CX',
  Pid3 varchar(128) default NULL,
  Pid5 varchar(128) default NULL,
  Pid7 varchar(128) default NULL,
  Pid8 varchar(128) default NULL,
  Pid11 varchar(128) default NULL,
  TypeCode varchar(64) default NULL,
  TypeCodeScheme varchar(64) default NULL,
  TypeCodeDisplayName varchar(64) default NULL,
  DocumentUri varchar(128) default NULL COMMENT 'May derive this value',
  RawData longblob,
  Persistent int(11) NOT NULL,
  RepositoryId varchar(128) NOT NULL COMMENT 'Added by the MIDHT for dynamic document Assembly',
  PRIMARY KEY  (documentid)
);

DROP TABLE IF EXISTS dyndocrepos.eventcode;
CREATE TABLE IF NOT EXISTS dyndocrepos.eventcode (
  eventcodeid int(11) NOT NULL,
  documentid int(11) NOT NULL COMMENT 'Foriegn key to document table',
  EventCode varchar(64) default NULL,
  EventCodeScheme varchar(64) default NULL,
  EventCodeDisplayName varchar(64) default NULL,
  PRIMARY KEY  (eventcodeid)
);

DROP TABLE IF EXISTS dyndocrepos.extraslot;
CREATE TABLE IF NOT EXISTS dyndocrepos.extraslot (
  extraslotid bigint(20) DEFAULT NULL,
  documentid int(11) NOT NULL,
  ExtraName varchar(64) default NULL, 
  ExtraSlotValue varchar(64) default NULL
);

/* Grant necessary rights to the nhincuser */
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, DROP ON dyndocrepos.* to nhincuser;
-- end docrepository
