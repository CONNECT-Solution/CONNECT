-- create application user
CREATE USER nhincuser IDENTIFIED BY 'nhincpass';

-- begin assigning authority
CREATE DATABASE assigningauthoritydb;

CREATE TABLE assigningauthoritydb.aa_to_home_community_mapping (
  id int(10) unsigned NOT NULL auto_increment,
  assigningauthorityid varchar(64) NOT NULL,
  homecommunityid varchar(64) NOT NULL,
  PRIMARY KEY  (id,assigningauthorityid)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON assigningauthoritydb.* to nhincuser;
-- end assigning authority

-- begin auditrepo
CREATE DATABASE auditrepo;

CREATE TABLE auditrepo.auditrepository
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    audit_timestamp DATETIME,
    eventId BIGINT NOT NULL,
    userId VARCHAR(100),
    participationTypeCode SMALLINT,
    participationTypeCodeRole SMALLINT,
    participationIDTypeCode VARCHAR(100),
    receiverPatientId VARCHAR(128),
    senderPatientId VARCHAR(128),
    communityId VARCHAR(255),
    messageType VARCHAR(100) NOT NULL,
    message LONGBLOB,
    PRIMARY KEY (id),
    UNIQUE UQ_eventlog_id(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON auditrepo.* to nhincuser;
-- end auditrepo

-- begin configdb
CREATE DATABASE configdb;

-- -----------------------------------------------------
-- Table `configdb`.`domain`
-- -----------------------------------------------------

CREATE TABLE configdb.domain (
    id SERIAL PRIMARY KEY,
    domainName VARCHAR(255),
    createTime DATETIME,
    postmasterAddressId BIGINT REFERENCES configdb.address(id),
    status SMALLINT DEFAULT 0,
    updateTime DATETIME
);

-- -----------------------------------------------------
-- Table `configdb`.`address`
-- -----------------------------------------------------

CREATE TABLE configdb.address (
    id SERIAL PRIMARY KEY,
    displayName VARCHAR(100),
    eMailAddress VARCHAR(255),
    endpoint VARCHAR(255),
    status SMALLINT DEFAULT 0,
    type VARCHAR(64),
    createTime DATETIME,
    updateTime DATETIME,
    domainId BIGINT NOT NULL REFERENCES configdb.domain(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`anchor`
-- -----------------------------------------------------

CREATE TABLE configdb.anchor (
    id SERIAL PRIMARY KEY,
    owner VARCHAR(255),
    thumbprint VARCHAR(64),
    certificateId BIGINT,
    createTime DATETIME,
    certificateData MEDIUMBLOB,
    validStartDate DATETIME,
    validEndDate DATETIME,
    forIncoming SMALLINT DEFAULT 1,
    forOutgoing SMALLINT DEFAULT 1,
    status SMALLINT DEFAULT 0
);

-- -----------------------------------------------------
-- Table `configdb`.`certificate`
-- -----------------------------------------------------

CREATE TABLE configdb.certificate (
    id SERIAL PRIMARY KEY,
    owner VARCHAR(255),
    thumbprint VARCHAR(64),
    createTime DATETIME,
    certificateData MEDIUMBLOB,
    validStartDate DATETIME,
    validEndDate DATETIME,
    status SMALLINT DEFAULT 0,
    privateKey SMALLINT
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicy`
-- -----------------------------------------------------

CREATE TABLE configdb.certpolicy (
    id SERIAL PRIMARY KEY,
    createTime DATETIME NOT NULL,
    lexicon INTEGER NOT NULL,
    data BLOB(204800) NOT NULL,
    policyName VARCHAR(255)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroup`
-- -----------------------------------------------------

CREATE TABLE configdb.certpolicygroup (
    id SERIAL PRIMARY KEY,
    createTime DATETIME NOT NULL,
    policyGroupName VARCHAR(255)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroupdomainreltn`
-- -----------------------------------------------------

CREATE TABLE configdb.certpolicygroupdomainreltn (
    id SERIAL PRIMARY KEY,
    policy_group_id BIGINT NOT NULL REFERENCES configdb.domain(id),
    domain_id BIGINT NOT NULL REFERENCES configdb.certpolicygroup(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroupreltn`
-- -----------------------------------------------------

CREATE TABLE configdb.certpolicygroupreltn (
    id SERIAL PRIMARY KEY,
    incoming SMALLINT,
    outgoing SMALLINT,
    policyUse INTEGER NOT NULL,
    certPolicyId BIGINT NOT NULL REFERENCES configdb.certpolicy(id),
    certPolicyGroupId BIGINT NOT NULL REFERENCES configdb.certpolicygroup(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`dnsrecord`
-- -----------------------------------------------------

CREATE TABLE configdb.dnsrecord (
    id SERIAL PRIMARY KEY,
    createTime DATETIME,
    data BLOB(8192),
    dclass INTEGER,
    name VARCHAR(255),
    ttl BIGINT,
    type INTEGER
);

-- -----------------------------------------------------
-- Table `configdb`.`setting`
-- -----------------------------------------------------

CREATE TABLE configdb.setting (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    status INTEGER,
    createTime DATETIME,
    updateTime DATETIME,
    value VARCHAR(4096)
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundle`
-- -----------------------------------------------------

CREATE TABLE configdb.trustbundle (
    id SERIAL PRIMARY KEY,
    bundleName VARCHAR(255) NOT NULL,
    bundleURL VARCHAR(255) NOT NULL,
    getCheckSum VARCHAR(255) NOT NULL,
    createTime DATETIME NOT NULL,
    lastRefreshAttempt DATETIME,
    lastRefreshError INTEGER,
    lastSuccessfulRefresh DATETIME,
    refreshInterval INTEGER,
    signingCertificateData BLOB(4096)
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundleanchor`
-- -----------------------------------------------------

CREATE TABLE configdb.trustbundleanchor (
    id SERIAL PRIMARY KEY,
    anchorData BLOB(4096) NOT NULL,
    thumbprint VARCHAR(255) NOT NULL,
    validEndDate DATETIME NOT NULL,
    validStartDate DATETIME NOT NULL,
    trustbundleId BIGINT NOT NULL REFERENCES configdb.trustbundle(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundledomainreltn`
-- -----------------------------------------------------

CREATE TABLE configdb.trustbundledomainreltn (
    id SERIAL PRIMARY KEY,
    forIncoming SMALLINT,
    forOutgoing SMALLINT,
    domain_id BIGINT NOT NULL REFERENCES configdb.domain (id),
    trust_bundle_id BIGINT NOT NULL REFERENCES configdb.trustbundle(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON configdb.* to nhincuser;
-- end configdb

-- begin docrepository
CREATE DATABASE docrepository;

CREATE TABLE docrepository.document (
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
  PatientId varchar(128) default NULL COMMENT 'Format of HL7 2.x CX',
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
  DocumentSize int(11) default NULL,
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
  OnDemand tinyint(1) NOT NULL default 0 COMMENT 'Indicate whether document is dynamic (true or 1) or static (false or 0).',
  NewDocumentUniqueId varchar(128) default NULL,
  NewRepositoryUniqueId varchar(128) default NULL,
  PRIMARY KEY  (documentid)
);

CREATE TABLE docrepository.eventcode (
  eventcodeid int(11) NOT NULL,
  documentid int(11) NOT NULL COMMENT 'Foreign key to document table',
  EventCode varchar(64) default NULL,
  EventCodeScheme varchar(64) default NULL,
  EventCodeDisplayName varchar(64) default NULL,
  PRIMARY KEY  (eventcodeid)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON docrepository.* to nhincuser;
-- end docrepository

-- begin patientcorrelationdb
CREATE DATABASE patientcorrelationdb;

CREATE TABLE patientcorrelationdb.correlatedidentifiers (
  correlationId int(10) unsigned NOT NULL auto_increment,
  PatientAssigningAuthorityId varchar(64) NOT NULL,
  PatientId varchar(128) NOT NULL,
  CorrelatedPatientAssignAuthId varchar(64) NOT NULL,
  CorrelatedPatientId varchar(128) NOT NULL,
  CorrelationExpirationDate datetime,
  PRIMARY KEY  (correlationId)
);

CREATE TABLE patientcorrelationdb.pddeferredcorrelation (
  Id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  MessageId VARCHAR(100) NOT NULL,
  AssigningAuthorityId varchar(64) NOT NULL,
  PatientId varchar(128) NOT NULL,
  CreationTime DATETIME NOT NULL,
  PRIMARY KEY (Id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON patientcorrelationdb.* to nhincuser;
-- end patientcorrelationdb

-- begin asyncmsgs
CREATE DATABASE asyncmsgs;

CREATE TABLE IF NOT EXISTS asyncmsgs.asyncmsgrepo (
    Id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    MessageId VARCHAR(100) NOT NULL,
    CreationTime DATETIME NOT NULL,
    ResponseTime DATETIME NULL DEFAULT NULL,
    Duration BIGINT NULL DEFAULT 0,
    ServiceName VARCHAR(45) NULL DEFAULT NULL,
    Direction VARCHAR(10) NULL DEFAULT NULL,
    CommunityId VARCHAR(100) NULL DEFAULT NULL,
    Status VARCHAR(45) NULL DEFAULT NULL,
    ResponseType VARCHAR(10) NULL DEFAULT NULL,
    Reserved VARCHAR(100) NULL DEFAULT NULL,
    MsgData LONGBLOB NULL DEFAULT NULL,
    RspData LONGBLOB NULL DEFAULT NULL,
    AckData LONGBLOB NULL DEFAULT NULL,
    PRIMARY KEY (Id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON asyncmsgs.* to nhincuser;
-- end asyncmsgs

-- begin logging
CREATE DATABASE logging;

CREATE TABLE logging.log (
    dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    context varchar(100) DEFAULT NULL,
    logLevel varchar(10) DEFAULT NULL,
    class varchar(500) DEFAULT NULL,
    message longtext
);

GRANT SELECT,INSERT,UPDATE,DELETE ON logging.* to nhincuser;
-- end logging

-- begin patientdb
CREATE DATABASE patientdb;

CREATE TABLE patientdb.patient (
  patientId BIGINT NOT NULL AUTO_INCREMENT,
  dateOfBirth DATE NULL,
  gender CHAR(2) NULL,
  ssn CHAR(9) NULL,
  PRIMARY KEY (patientId),
  UNIQUE INDEX patientId_UNIQUE (patientId ASC) )
COMMENT = 'Patient Repository';

CREATE TABLE patientdb.identifier (
  identifierId BIGINT NOT NULL AUTO_INCREMENT,
  patientId BIGINT NOT NULL,
  id VARCHAR(64) NULL,
  organizationId VARCHAR(64) NULL,
  PRIMARY KEY (identifierId),
  UNIQUE INDEX identifierrId_UNIQUE (identifierId ASC),
  INDEX fk_identifier_patient (patientId ASC),
  CONSTRAINT fk_identifier_patient
    FOREIGN KEY (patientId )
    REFERENCES patientdb.patient (patientId )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'Identifier definitions';

CREATE TABLE patientdb.personname (
  personnameId BIGINT NOT NULL AUTO_INCREMENT,
  patientId BIGINT NOT NULL,
  prefix VARCHAR(64) NULL,
  firstName VARCHAR(64) NULL,
  middleName VARCHAR(64) NULL,
  lastName VARCHAR(64) NULL,
  suffix VARCHAR(64) NULL,
  PRIMARY KEY (personnameId),
  UNIQUE INDEX personnameId_UNIQUE (personnameId ASC),
  INDEX fk_personname_patient (patientId ASC),
  CONSTRAINT fk_personname_patient
    FOREIGN KEY (patientId )
    REFERENCES patientdb.patient (patientId )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'Person Names';

CREATE TABLE patientdb.address (
  addressId BIGINT NOT NULL AUTO_INCREMENT,
  patientId BIGINT NOT NULL,
  street1 VARCHAR(128) NULL,
  street2 VARCHAR(128) NULL,
  city VARCHAR(128) NULL,
  state VARCHAR(128) NULL,
  postal VARCHAR(45) NULL,
  PRIMARY KEY (addressId),
  UNIQUE INDEX addressId_UNIQUE (addressId ASC),
  INDEX fk_address_patient (patientId ASC),
  CONSTRAINT fk_address_patient
    FOREIGN KEY (patientId )
    REFERENCES patientdb.patient (patientId )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'Addresses';

CREATE TABLE patientdb.phonenumber (
  phonenumberId BIGINT NOT NULL AUTO_INCREMENT,
  patientId BIGINT NOT NULL,
  value VARCHAR(64) NULL,
  PRIMARY KEY (phonenumberId),
  UNIQUE INDEX phonenumberId_UNIQUE (phonenumberId ASC),
  INDEX fk_phonenumber_patient (patientId ASC),
  CONSTRAINT fk_phonenumber_patient
    FOREIGN KEY (patientId )
    REFERENCES patientdb.patient (patientId )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'Phone Numbers';

GRANT SELECT,INSERT,UPDATE,DELETE ON patientdb.* to nhincuser;
-- end patientdb

-- begin transrepo

CREATE DATABASE transrepo;

CREATE TABLE transrepo.transactionrepository (
    id BIGINT NOT NULL AUTO_INCREMENT,
    transactionId VARCHAR(100) NOT NULL,
    messageId VARCHAR(100) NOT NULL,
    transactionTime TIMESTAMP NULL,
    PRIMARY KEY (id),
    INDEX messageId_idx (messageId),
    UNIQUE transID_UNIQUE (transactionId, messageId) )
COMMENT = 'Message Transaction Repository';

GRANT SELECT,INSERT,UPDATE,DELETE ON transrepo.* to nhincuser;
-- end transrepo

-- begin eventdb

CREATE DATABASE eventdb;

CREATE TABLE eventdb.event (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description longtext,
  transactionId VARCHAR(100),
  messageId VARCHAR(100),
  eventTime TIMESTAMP,
  PRIMARY KEY (id) )
COMMENT = 'Event Logging';

GRANT SELECT,INSERT,UPDATE,DELETE ON eventdb.* to nhincuser;
GRANT SELECT,INSERT,UPDATE,DELETE ON *.* TO 'nhincuser'@'localhost' IDENTIFIED BY 'nhincpass' WITH GRANT OPTION;
GRANT SELECT,INSERT,UPDATE,DELETE ON *.* TO 'nhincuser'@'127.0.0.1' IDENTIFIED BY 'nhincpass' WITH GRANT OPTION;
-- end eventdb

GRANT ALL PRIVILEGES ON *.* TO 'nhincuser'@'localhost' IDENTIFIED BY 'nhincpass' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'nhincuser'@'127.0.0.1' IDENTIFIED BY 'nhincpass' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'nhincuser'@'{host name}' IDENTIFIED BY 'nhincpass' WITH GRANT OPTION;
FLUSH PRIVILEGES;
