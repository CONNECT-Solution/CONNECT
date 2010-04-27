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
	MessageId varchar2(100) NOT NULL,
	CreationTime DATE NOT NULL,
	ServiceName varchar2(100),
	MsgData BLOB,
	PRIMARY KEY (MessageId)
);

commit;
