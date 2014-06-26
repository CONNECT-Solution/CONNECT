-- create application user
CREATE USER nhincuser IDENTIFIED BY 'nhincpass';

-- begin assigning authority
CREATE DATABASE assigningauthoritydb;

CREATE TABLE IF NOT EXISTS assigningauthoritydb.aa_to_home_community_mapping (
  id int(10) unsigned NOT NULL auto_increment,
  assigningauthorityid varchar(64) NOT NULL,
  homecommunityid varchar(64) NOT NULL,
  PRIMARY KEY  (id,assigningauthorityid)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON assigningauthoritydb.* to nhincuser;
-- end assigning authority

-- begin auditrepo
CREATE DATABASE auditrepo;

CREATE TABLE IF NOT EXISTS auditrepo.auditrepository
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

CREATE TABLE IF NOT EXISTS configdb.domain (
    id SERIAL PRIMARY KEY,
    postmasterAddressId BIGINT,
    domainName VARCHAR(255) NOT NULL,
    status INTEGER DEFAULT 0,
    createTime DATETIME NOT NULL,
    updateTime DATETIME
);

-- -----------------------------------------------------
-- Table `configdb`.`address`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.address (
    id SERIAL PRIMARY KEY,
    emailAddress VARCHAR(255) NOT NULL,
    displayName VARCHAR(100),
    endpoint VARCHAR(255),
    type VARCHAR(4),
    status INTEGER DEFAULT 0,
    createTime DATETIME NOT NULL,
    updateTime DATETIME,

    domainId BIGINT UNSIGNED NOT NULL,
    INDEX fk_domainId (domainId ASC),
    CONSTRAINT fk_domainId
        FOREIGN KEY (domainId)
        REFERENCES configdb.domain(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table `configdb`.`anchor`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.anchor (
    id SERIAL PRIMARY KEY,
    certificateId BIGINT NOT NULL COMMENT '?',
    owner VARCHAR(255) NOT NULL COMMENT 'Subject CN',
    thumbprint VARCHAR(64) NOT NULL,
    certificateData BLOB(4096) NOT NULL,
    validStartDate DATETIME NOT NULL,
    validEndDate DATETIME NOT NULL,
    incoming BOOLEAN NOT NULL DEFAULT TRUE,
    outgoing BOOLEAN NOT NULL DEFAULT TRUE,
    status INTEGER DEFAULT 0,
    createTime DATETIME NOT NULL
);

-- -----------------------------------------------------
-- Table `configdb`.`certificate`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.certificate (
    id SERIAL PRIMARY KEY,
    owner VARCHAR(255) NOT NULL COMMENT 'Subject CN',
    thumbprint VARCHAR(64) NOT NULL,
    certificateData BLOB(4096) NOT NULL,
    validStartDate DATETIME NOT NULL,
    validEndDate DATETIME NOT NULL,
    privateKey BOOLEAN NOT NULL DEFAULT FALSE,
    status INTEGER DEFAULT 0,
    createTime DATETIME NOT NULL
);

-- -----------------------------------------------------
-- Table `configdb`.`setting`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.setting (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    value VARCHAR(4096),
    status INTEGER DEFAULT 0,
    createTime DATETIME NOT NULL,
    updateTime DATETIME
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundle`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.trustbundle (
    id SERIAL PRIMARY KEY,
    bundleName VARCHAR(255) NOT NULL,
    bundleURL VARCHAR(255) NOT NULL,
    bundleChecksum VARCHAR(255) NOT NULL,
    lastRefreshAttempt DATETIME,
    lastSuccessfulRefresh DATETIME,
    refreshInterval INTEGER,
    lastRefreshError INTEGER COMMENT 'enum value for refresh status message',
    signingCertificateData BLOB(4096),
    createTime DATETIME NOT NULL
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundleanchor`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.trustbundleanchor (
    id SERIAL PRIMARY KEY,
    anchorData BLOB(4096) NOT NULL,
    thumbprint VARCHAR(64) NOT NULL,
    validStartDate DATETIME NOT NULL,
    validEndDate DATETIME NOT NULL,

    trustbundleId BIGINT UNSIGNED NOT NULL,
    INDEX fk_trustbundleId (trustbundleId ASC),
    CONSTRAINT fk_trustbundleId
        FOREIGN KEY (trustbundleId)
        REFERENCES configdb.trustbundle(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundledomainreltn`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.trustbundledomainreltn (
    id SERIAL PRIMARY KEY,
    incoming BOOLEAN NOT NULL DEFAULT TRUE,
    outgoing BOOLEAN NOT NULL DEFAULT TRUE,

    domain_id BIGINT UNSIGNED NOT NULL REFERENCES configdb.domain (id),
    INDEX fk_domain_id (domain_id ASC),
    CONSTRAINT fk_domain_id
        FOREIGN KEY (domain_id)
        REFERENCES configdb.domain(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,

    trust_bundle_id BIGINT UNSIGNED NOT NULL REFERENCES configdb.trustbundle(id),
    INDEX fk_trust_bundle_id (trust_bundle_id ASC),
    CONSTRAINT fk_trust_bundle_id
        FOREIGN KEY (trust_bundle_id)
        REFERENCES configdb.trustbundle(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicy`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.certpolicy (
    id SERIAL PRIMARY KEY,
    createTime DATETIME NOT NULL,
    lexicon INTEGER NOT NULL,
    policyData BLOB(204800) NOT NULL,
    policyName VARCHAR(255)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroup`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.certpolicygroup (
    id SERIAL PRIMARY KEY,
    createTime DATETIME NOT NULL,
    policyGroupName VARCHAR(255)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroupdomainreltn`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.certpolicygroupdomainreltn (
    id SERIAL PRIMARY KEY,
    policy_group_id BIGINT NOT NULL REFERENCES configdb.certpolicygroup(id),
    domain_id BIGINT NOT NULL REFERENCES configdb.domain(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroupreltn`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.certpolicygroupreltn (
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

CREATE TABLE IF NOT EXISTS configdb.dnsrecord (
    id SERIAL PRIMARY KEY,
    createTime DATETIME NOT NULL,
    data BLOB(8192),
    dclass INTEGER,
    name VARCHAR(255),
    ttl BIGINT,
    type INTEGER
);

GRANT SELECT,INSERT,UPDATE,DELETE ON configdb.* to nhincuser;
-- end configdb

-- begin docrepository
CREATE DATABASE docrepository;

CREATE TABLE IF NOT EXISTS docrepository.document (
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

CREATE TABLE IF NOT EXISTS docrepository.eventcode (
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

CREATE TABLE IF NOT EXISTS patientcorrelationdb.correlatedidentifiers (
  correlationId int(10) unsigned NOT NULL auto_increment,
  PatientAssigningAuthorityId varchar(64) NOT NULL,
  PatientId varchar(128) NOT NULL,
  CorrelatedPatientAssignAuthId varchar(64) NOT NULL,
  CorrelatedPatientId varchar(128) NOT NULL,
  CorrelationExpirationDate datetime,
  PRIMARY KEY  (correlationId)
);

CREATE TABLE IF NOT EXISTS patientcorrelationdb.pddeferredcorrelation (
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

CREATE TABLE IF NOT EXISTS logging.log (
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

CREATE TABLE IF NOT EXISTS patientdb.patient (
  patientId BIGINT NOT NULL AUTO_INCREMENT,
  dateOfBirth DATE NULL,
  gender CHAR(2) NULL,
  ssn CHAR(9) NULL,
  PRIMARY KEY (patientId),
  UNIQUE INDEX patientId_UNIQUE (patientId ASC) )
COMMENT = 'Patient Repository';

CREATE TABLE IF NOT EXISTS patientdb.identifier (
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

CREATE TABLE IF NOT EXISTS patientdb.personname (
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

CREATE TABLE IF NOT EXISTS patientdb.address (
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

CREATE TABLE IF NOT EXISTS patientdb.phonenumber (
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

CREATE TABLE IF NOT EXISTS transrepo.transactionrepository (
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

CREATE TABLE IF NOT EXISTS eventdb.event (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description longtext,
  transactionId VARCHAR(100),
  messageId VARCHAR(100),
  serviceType VARCHAR(100),
  initiatingHcid VARCHAR(100),
  respondingHcids VARCHAR(100),
  eventTime TIMESTAMP,
  PRIMARY KEY (id) )
COMMENT = 'Event Logging';

GRANT SELECT,INSERT,UPDATE,DELETE ON eventdb.* to nhincuser;

GRANT SELECT,INSERT,UPDATE,DELETE ON *.* TO 'nhincuser'@'localhost' IDENTIFIED BY 'nhincpass' WITH GRANT OPTION;
GRANT SELECT,INSERT,UPDATE,DELETE ON *.* TO 'nhincuser'@'127.0.0.1' IDENTIFIED BY 'nhincpass' WITH GRANT OPTION;
-- end eventdb

-- begin adminguidb
CREATE DATABASE adminguidb;

-- -----------------------------------------------------
-- Table `adminguidb`.`UserLogin`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS adminguidb.UserRole (
	roleId SERIAL PRIMARY KEY,
	roleName VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS adminguidb.PagePreference (
	prefId SERIAL PRIMARY KEY,
	pageName VARCHAR(100) NOT NULL,
	pageDesc VARCHAR(100) NOT NULL,
	accessPage BIGINT NOT NULL,
	prefRoleId BIGINT unsigned NOT NULL,
	CONSTRAINT fk_role_pref
      FOREIGN KEY (prefRoleId)
      REFERENCES adminguidb.UserRole (roleId)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION	
);

CREATE TABLE IF NOT EXISTS adminguidb.UserLogin (
    id SERIAL PRIMARY KEY,
    salt varchar(100) NOT NULL,
    sha1 varchar(100) NOT NULL,
    userName varchar(100) NOT NULL UNIQUE,
    userRole BIGINT unsigned NOT NULL,
    CONSTRAINT fk_role_user
      FOREIGN KEY (userRole)
      REFERENCES adminguidb.UserRole (roleId)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

INSERT INTO adminguidb.UserRole 
(roleId, roleName)
VALUES
(1, "ADMIN"),
(2, "SUPER USER"),
(3, "USER");

INSERT INTO adminguidb.PagePreference
(pageName, pageDesc, accessPage, prefRoleId)
VALUES
("status.xhtml", "Status", 0, 1),
("status.xhtml", "Status", 0, 2),
("status.xhtml", "Status", 0, 3),
("StatusPrime.xhtml", "Status Prime", 0, 1),
("StatusPrime.xhtml", "Status Prime", 0, 2),
("StatusPrime.xhtml", "Status Prime", 0, 3),
("acctmanage.xhtml", "Account Management", 0, 1),
("acctmanage.xhtml", "Account Management", -1, 2),
("acctmanage.xhtml", "Account Management", -1, 3),
("acctmanagePrime.xhtml", "Account Management Prime", 0, 1),
("acctmanagePrime.xhtml", "Account Management Prime", -1, 2),
("acctmanagePrime.xhtml", "Account Management Prime", -1, 3),
("ManageRole.xhtml", "Manage Role", 0, 1),
("ManageRole.xhtml", "Manage Role", -1, 2),
("ManageRole.xhtml", "Manage Role", -1, 3),
("direct.xhtml", "Direct Config", 0, 1),
("direct.xhtml", "Direct Config", 0, 2),
("direct.xhtml", "Direct Config", 0, 3),
("direct_domains_create.xhtml", "Direct Create Domain", 0, 1),
("direct_domains_create.xhtml", "Direct Create Domain", 0, 2),
("direct_domains_create.xhtml", "Direct Create Domain", 0, 3);


INSERT INTO adminguidb.UserLogin
(id, salt, sha1, userName, userRole)
VALUES
(1, "ABCD", "TxMu4SPUdek0XU5NovS9U2llt3Q=", "CONNECTAdmin", 1);

GRANT SELECT,INSERT,UPDATE,DELETE ON adminguidb.* to nhincuser;
-- end adminguidb

GRANT ALL PRIVILEGES ON *.* TO 'nhincuser'@'localhost' IDENTIFIED BY 'nhincpass' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'nhincuser'@'127.0.0.1' IDENTIFIED BY 'nhincpass' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'nhincuser'@'{host name}' IDENTIFIED BY 'nhincpass' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- begin assigning authority
CREATE DATABASE messagemonitoringdb;

CREATE TABLE messagemonitoringdb.trackmessage (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier',
  senderemailid varchar(255) DEFAULT NULL COMMENT 'sender email identifier',
  subject varchar(255) DEFAULT NULL COMMENT 'email Subject',
  messageid varchar(100) DEFAULT NULL COMMENT 'unique email message identifier',
  recipients varchar(4000) DEFAULT NULL,
  deliveryrequested tinyint(1) DEFAULT '0' COMMENT 'column to identify if the edge requested for delivery notification',
  status varchar(30) DEFAULT NULL COMMENT 'Pending, Completed, Error',
  createtime timestamp NULL DEFAULT NULL COMMENT 'Creation Time',
  updatetime timestamp NULL DEFAULT NULL COMMENT 'Record Update time',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE messagemonitoringdb.trackmessagenotification (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier',
  emailid varchar(255) NOT NULL COMMENT 'notification email identifier',
  messageid varchar(100) DEFAULT NULL COMMENT 'unique email message identifier',
  trackmessageid bigint(20) NOT NULL COMMENT 'unique trackmessage identifier',
  status varchar(30) NOT NULL COMMENT 'Pending, Completed, Error',
  createtime timestamp NULL DEFAULT NULL,
  updatetime timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id),
  KEY fk_trackmessageId (trackmessageid),
  CONSTRAINT fk_trackmessageId FOREIGN KEY (trackmessageid) REFERENCES trackmessage (id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table to track outbound Message Monitoring notification';

GRANT SELECT,INSERT,UPDATE,DELETE ON messagemonitoringdb.* to nhincuser;
-- end assigning authority

-- -----------------------------------------------------
-- The following is a workaround that is required for
-- deployment due to a bug in Direct RI
-- -----------------------------------------------------
USE configdb;
LOCK TABLES domain WRITE, address WRITE;

INSERT INTO domain
(id, domainName, postmasterAddressId, status, createTime, updateTime)
VALUES
(1, 'direct.example.org', NULL, 1, now(), now());

INSERT INTO address
(id, displayName, emailAddress, endpoint, status, type, createTime, updateTime, domainId)
VALUES
(1, 'direct.example.org', 'postmaster@direct.example.org', NULL, 1, NULL, now(), now(), 1);

UPDATE address SET id = 2 WHERE id = 1;

UPDATE domain SET postmasterAddressId = 2 WHERE id = 1;

UNLOCK TABLES;
