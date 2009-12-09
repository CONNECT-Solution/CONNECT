/*
Table creation script for the BHIE Facade Stop-Gap document repository.

This script was intended to be run on MySQL.

Please see the DatabaseInstructions.txt file for database creation and configuration instructions.
*/

DROP TABLE IF EXISTS document CASCADE;
CREATE TABLE document (
	documentid INTEGER NOT NULL,
	DocumentUniqueId VARCHAR(64) NOT NULL,
	DocumentTitle VARCHAR(128),
	authorPerson VARCHAR(64),
	authorInstitution VARCHAR(64),
	authorRole VARCHAR(64),
	authorSpecialty VARCHAR(64),
	AvailabilityStatus VARCHAR(64),
	ClassCode VARCHAR(64),
	ClassCodeScheme VARCHAR(64),
	ClassCodeDisplayName VARCHAR(64),
	ConfidentialityCode VARCHAR(64),
	ConfidentialityCodeScheme VARCHAR(64),
	ConfidentialityCodeDisplayName VARCHAR(64),
	CreationTime DATETIME COMMENT 'Date format expected: MM/dd/yyyy.HH:mm:ss',
	FormatCode VARCHAR(64),
	FormatCodeScheme VARCHAR(64),
	FormatCodeDisplayName VARCHAR(64),
	PatientId VARCHAR(64) COMMENT 'Format of HL7 2.x CX',
	ServiceStartTime DATETIME COMMENT 'Format of YYYYMMDDHHMMSS',
	ServiceStopTime DATETIME COMMENT 'Format of YYYYMMDDHHMMSS', 
	Status VARCHAR(64),
	Comments VARCHAR(256),
	Hash VARCHAR(1028) COMMENT 'Might be better to derive',
	FacilityCode VARCHAR(64),
	FacilityCodeScheme VARCHAR(64),
	FacilityCodeDisplayName VARCHAR(64),
	IntendedRecipientPerson VARCHAR(128) COMMENT 'Format of HL7 2.x XCN',
	IntendedRecipientOrganization VARCHAR(128) COMMENT 'Format of HL7 2.x XON',
	LanguageCode VARCHAR(64),
	LegalAuthenticator VARCHAR(128) COMMENT 'Format of HL7 2.x XCN',
	MimeType VARCHAR(32),
	ParentDocumentId VARCHAR(64),
	ParentDocumentRelationship VARCHAR(64),
	PracticeSetting VARCHAR(64),
	PracticeSettingScheme VARCHAR(64),
	PracticeSettingDisplayName VARCHAR(64),
	Size Integer,
	SourcePatientId VARCHAR(128) COMMENT 'Format of HL7 2.x CX',
	Pid3 VARCHAR(128),
	Pid5 VARCHAR(128),
	Pid7 VARCHAR(128),
	Pid8 VARCHAR(128),
	Pid11 VARCHAR(128),
	TypeCode VARCHAR(64),
	TypeCodeScheme VARCHAR(64),
	TypeCodeDisplayName VARCHAR(64),
	DocumentUri VARCHAR(128) COMMENT 'May derive this value',
	RawData LONGBLOB,
	Persistent Integer NOT NULL
);

ALTER TABLE document
	ADD CONSTRAINT document_pk PRIMARY KEY(documentid);

DROP TABLE IF EXISTS eventcode CASCADE;
CREATE TABLE eventcode (
	eventcodeid INTEGER NOT NULL,
	documentid INTEGER NOT NULL COMMENT 'Foriegn key to document table',
	EventCode VARCHAR(64),
	EventCodeScheme VARCHAR(64),
	EventCodeDisplayName VARCHAR(64)
);

ALTER TABLE eventcode
	ADD CONSTRAINT eventcode_pk PRIMARY KEY (eventcodeid);