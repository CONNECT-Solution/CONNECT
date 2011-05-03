-- create application user
CREATE USER nhincuser IDENTIFIED BY nhincpass;
GRANT ALL PRIVILEGES to nhincuser;;


CREATE TABLE nhincuser.agg_transaction (
    TransactionId varchar2(32) NOT NULL,
    ServiceType varchar2(64) NOT NULL,
    TransactionStartTime DATE,
  PRIMARY KEY(TransactionId)
);

CREATE TABLE nhincuser.agg_message_results (
    MessageId VARCHAR2(32) NOT NULL,
    TransactionId VARCHAR2(32) NOT NULL,
    MessageKey VARCHAR2(1000) NOT NULL,
    MessageOutTime DATE,
    ResponseReceivedTime DATE,
    ResponseMessageType VARCHAR2(100),
    ResponseMessage BLOB,
  PRIMARY KEY (MessageId)
);

CREATE TABLE nhincuser.aa_to_home_community_mapping (
  id number(10) NOT NULL,
  assigningauthorityid varchar2(45) NOT NULL,
  homecommunityid varchar2(45) NOT NULL,
  PRIMARY KEY  (id,assigningauthorityid)
);

CREATE TABLE nhincuser.auditrepository
(
    id number NOT NULL,
    audit_timestamp DATE,
    eventId number NOT NULL,
    userId varchar2(100),
    participationTypeCode number,
    participationTypeCodeRole number,
    participationIDTypeCode varchar2(100),
    receiverPatientId varchar2(100),
    senderPatientId varchar2(100),
    communityId varchar2(255),
    messageType varchar2(100) NOT NULL,
    message BLOB,
    PRIMARY KEY (id)
);

CREATE TABLE nhincuser.document (
  documentid number(11) NOT NULL,
  DocumentUniqueId varchar2(64) NOT NULL,
  DocumentTitle varchar2(128) default NULL,
  authorPerson varchar2(64) default NULL,
  authorInstitution varchar2(64) default NULL,
  authorRole varchar2(64) default NULL,
  authorSpecialty varchar2(64) default NULL,
  AvailabilityStatus varchar2(64) default NULL,
  ClassCode varchar2(64) default NULL,
  ClassCodeScheme varchar2(64) default NULL,
  ClassCodeDisplayName varchar2(64) default NULL,
  ConfidentialityCode varchar2(64) default NULL,
  ConfidentialityCodeScheme varchar2(64) default NULL,
  ConfidentialityCodeDisplayName varchar2(64) default NULL,
  CreationTime DATE default NULL,
  FormatCode varchar2(64) default NULL,
  FormatCodeScheme varchar2(64) default NULL,
  FormatCodeDisplayName varchar2(64) default NULL,
  PatientId varchar2(64) default NULL,
  ServiceStartTime DATE default NULL,
  ServiceStopTime DATE default NULL,
  Status varchar2(64) default NULL,
  Comments varchar2(256) default NULL,
  Hash varchar2(1028) default NULL,
  FacilityCode varchar2(64) default NULL,
  FacilityCodeScheme varchar2(64) default NULL,
  FacilityCodeDisplayName varchar2(64) default NULL,
  IntendedRecipientPerson varchar2(128) default NULL,
  IntendedRecipientOrganization varchar2(128) default NULL,
  LanguageCode varchar2(64) default NULL,
  LegalAuthenticator varchar2(128) default NULL,
  MimeType varchar2(32) default NULL,
  ParentDocumentId varchar2(64) default NULL,
  ParentDocumentRelationship varchar2(64) default NULL,
  PracticeSetting varchar2(64) default NULL,
  PracticeSettingScheme varchar2(64) default NULL,
  PracticeSettingDisplayName varchar2(64) default NULL,
  DocumentSize number(11) default NULL,
  SourcePatientId varchar2(128) default NULL,
  Pid3 varchar2(128) default NULL,
  Pid5 varchar2(128) default NULL,
  Pid7 varchar2(128) default NULL,
  Pid8 varchar2(128) default NULL,
  Pid11 varchar2(128) default NULL,
  TypeCode varchar2(64) default NULL,
  TypeCodeScheme varchar2(64) default NULL,
  TypeCodeDisplayName varchar2(64) default NULL,
  DocumentUri varchar2(128) default NULL,
  RawData BLOB,
  Persistent number(11) NOT NULL,
  PRIMARY KEY  (documentid)
);

CREATE TABLE nhincuser.eventcode (
  eventcodeid number(11) NOT NULL,
  documentid number(11) NOT NULL,
  EventCode varchar2(64) default NULL,
  EventCodeScheme varchar2(64) default NULL,
  EventCodeDisplayName varchar2(64) default NULL,
  PRIMARY KEY  (eventcodeid)
);

CREATE SEQUENCE nhincuser.hibernate_sequence;

CREATE TABLE nhincuser.gateway_lift_message
(
    id number NOT NULL,
    initialEntryTimestamp DATE NOT NULL,
    messageState varchar2(32) NOT NULL,
    processingStartTimestamp DATE,
    producerProxyAddress varchar2(500) NOT NULL,
    producerProxyPort number(5) NOT NULL,
    fileNameToRetrieve varchar2(200) NOT NULL,
    requestKeyGuid varchar2(64) NOT NULL,
    messageType varchar2(100) NOT NULL,
    message BLOB,
    assertion BLOB,
    PRIMARY KEY (id)
);

CREATE TABLE nhincuser.transfer_data
(
    id number NOT NULL,
    requestKeyGuid varchar2(64) NOT NULL,
    transferState varchar2(32) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE nhincuser.correlatedidentifiers (
  correlationId number(10) NOT NULL,
  PatientAssigningAuthorityId varchar2(45) NOT NULL,
  PatientId varchar2(45) NOT NULL,
  CorrelatedPatientAssignAuthId varchar2(45) NOT NULL,
  CorrelatedPatientId varchar2(45) NOT NULL,
  CorrelationExpirationDate DATE,
  PRIMARY KEY  (correlationId)
);

CREATE TABLE nhincuser.subscription (
    id varchar2(128) NOT NULL,
    Subscriptionid varchar2(128),
    SubscribeXML BLOB,
    SubscriptionReferenceXML BLOB,
    RootTopic BLOB,
    ParentSubscriptionId varchar2(128),
    ParentSubscriptionReferenceXML BLOB,
    Consumer varchar2(128),
    Producer varchar2(128),
    PatientId varchar2(128),
    PatientAssigningAuthority varchar2(128),
    Targets BLOB,
    CreationDate DATE,
  PRIMARY KEY(id)
);

CREATE TABLE nhincuser.asyncmsgrepo (
    Id number(10) NOT NULL,
    MessageId varchar2(100) NOT NULL,
    CreationTime DATE NOT NULL,
    ResponseTime DATE,
    Duration number(10),
    ServiceName varchar2(45),
    Direction varchar2(10),
    CommunityId varchar2(100),
    Status varchar2(45),
    ResponseType varchar2(10),
    Reserved varchar2(100),
    MsgData BLOB,
    RspData BLOB,
    AckData BLOB,
    PRIMARY KEY (Id)
);


CREATE TABLE nhincuser.patient (
  patientId number(11) NOT NULL,
  dateOfBirth DATE NULL,
  gender CHAR(2) NULL,
  ssn CHAR(9) NULL,
  PRIMARY KEY (patientId)
);

CREATE TABLE nhincuser.identifier (
  identifierId number(11) NOT NULL,
  patientId number(11) NOT NULL,
  id varchar2(64) NULL,
  organizationId varchar2(64) NULL,
  PRIMARY KEY (identifierId)
);

CREATE TABLE nhincuser.personname (
  personnameId number(11) NOT NULL,
  patientId number(11) NOT NULL,
  prefix varchar2(64) NULL,
  firstName varchar2(64) NULL,
  middleName varchar2(64) NULL,
  lastName varchar2(64) NULL,
  suffix varchar2(64) NULL,
  PRIMARY KEY (personnameId)
);

CREATE TABLE nhincuser.address (
  addressId number(11) NOT NULL,
  patientId number(11) NOT NULL,
  street1 varchar2(128) NULL,
  street2 varchar2(128) NULL,
  city varchar2(128) NULL,
  state varchar2(128) NULL,
  postal varchar2(45) NULL,
  PRIMARY KEY (addressId)
);

CREATE TABLE nhincuser.phonenumber (
  phonenumberId number(11) NOT NULL,
  patientId number(11) NOT NULL,
  value varchar2(64) NULL,
  PRIMARY KEY (phonenumberId)
);


CREATE TABLE nhincuser.perfrepository (
  id number(11) NOT NULL,
  starttime TIMESTAMP NULL,
  stoptime TIMESTAMP NULL,
  duration BIGINT NULL,
  servicetype varchar2(45) NULL,
  messagetype varchar2(10) NULL,
  direction varchar2(10) NULL,
  communityid varchar2(255) NULL,
  status number(2) NULL DEFAULT 0,
  PRIMARY KEY (id)
);


commit;
