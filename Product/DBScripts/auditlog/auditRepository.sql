DROP TABLE IF EXISTS auditrepository;
CREATE TABLE auditrepository
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	timestamp DATETIME,
	eventId BIGINT NOT NULL,
	userId VARCHAR(100),
	participationTypeCode SMALLINT,
	participationTypeCodeRole SMALLINT,
	participationIDTypeCode VARCHAR(100),
	receiverPatientId VARCHAR(100),
	senderPatientId VARCHAR(100),
	communityId VARCHAR(255),
	messageType VARCHAR(100) NOT NULL,
	message LONGBLOB,
	PRIMARY KEY (id),
	UNIQUE UQ_eventlog_id(id)
);



