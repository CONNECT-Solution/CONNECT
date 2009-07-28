/*
	Table creation script for the aggregation tables.  These should be 
	loaded into the AGGREGATI0N schema.   They are used when doing
	aggregation of responses from multple systems that come in on separate
	threads.
	
	This script was intended to be run on MySQL.
*/

DROP TABLE IF EXISTS agg_transaction CASCADE;
CREATE TABLE agg_transaction (
	TransactionId VARCHAR(32) NOT NULL COMMENT 'This will be a UUID',
	ServiceType VARCHAR(64) NOT NULL,
	TransactionStartTime DATETIME COMMENT 'Format of YYYYMMDDHHMMSS');

ALTER TABLE agg_transaction
	ADD CONSTRAINT agg_transaction_pk PRIMARY KEY(TransactionId);

DROP TABLE IF EXISTS agg_message_results CASCADE;
CREATE TABLE agg_message_results (
	MessageId VARCHAR(32) NOT NULL COMMENT 'This will be a UUID.',
	TransactionId VARCHAR(32) NOT NULL COMMENT 'This will be a UUID. - Foreign Key to the agg_transaction table.',
	MessageKey VARCHAR(1000) NOT NULL COMMENT 'This is the key used to tie the response to the original request.',
	MessageOutTime DATETIME COMMENT 'This is the date/time when the outbound request was recorded.  Format of YYYYMMDDHHMMSS',
	ResponseReceivedTime DATETIME COMMENT 'This is the date/time when the response was recorded.  Format of YYYYMMDDHHMMSS',
	ResponseMessageType VARCHAR(100) COMMENT 'This is the name of the outer layer JAXB class for the response message.',
	ResponseMessage LONGTEXT COMMENT 'The response message in XML - Based on marshalling using JAXB');

ALTER TABLE agg_message_results
	ADD CONSTRAINT agg_message_results_pk PRIMARY KEY (MessageId);
	

	