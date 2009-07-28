/*
Table creation script for the subscription repository.

This script was intended to be run on MySQL.

Please see the DatabaseInstructions.txt file for database creation and configuration instructions.
*/

DROP TABLE IF EXISTS subscription CASCADE;
CREATE TABLE subscription (
	id VARCHAR(128) NOT NULL COMMENT 'Database generated UUID',
	Subscriptionid VARCHAR(128) COMMENT 'Unique identifier for a CONNECT generated subscription',
	SubscribeXML LONGTEXT COMMENT 'Full subscribe message as an XML string',
	SubscriptionReferenceXML LONGTEXT COMMENT 'Full subscription reference as an XML string',
	RootTopic LONGTEXT COMMENT 'Root topic of the subscription record',
	ParentSubscriptionId VARCHAR(128) COMMENT 'Subscription id for a parent record provided for fast searching',
	ParentSubscriptionReferenceXML LONGTEXT COMMENT 'Full subscription reference for a parent record as an XML string',
	Consumer VARCHAR(128) COMMENT 'Notification consumer system',
	Producer VARCHAR(128) COMMENT 'Notification producer system',
	PatientId VARCHAR(128) COMMENT 'Local system patient identifier',
	PatientAssigningAuthority VARCHAR(128) COMMENT 'Assigning authority for the local patient identifier',
	Targets LONGTEXT COMMENT 'Full target system as an XML string',
	CreationDate DATETIME COMMENT 'Format of YYYYMMDDHHMMSS'
);

ALTER TABLE subscription
	ADD CONSTRAINT subscription_pk PRIMARY KEY(id);

