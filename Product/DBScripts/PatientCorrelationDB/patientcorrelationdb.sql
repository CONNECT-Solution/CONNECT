-- MySQL dump 10.11
--
-- Host: localhost    Database: patientcorrelationdb
-- ------------------------------------------------------
-- Server version	5.0.51b-community-nt

--
-- Table structure for table `patientcorrelationdb.correlatedidentifiers`
--

DROP TABLE IF EXISTS patientcorrelationdb.correlatedidentifiers;
CREATE TABLE patientcorrelationdb.correlatedidentifiers (
  correlationId int(10) unsigned NOT NULL auto_increment,
  PatientAssigningAuthorityId varchar(64) NOT NULL,
  PatientId varchar(128) NOT NULL,
  CorrelatedPatientAssignAuthId varchar(64) NOT NULL,
  CorrelatedPatientId varchar(128) NOT NULL,
  CorrelationExpirationDate datetime,
  PRIMARY KEY  (correlationId)
);

DROP TABLE IF EXISTS patientcorrelationdb.pddeferredcorrelation;
CREATE TABLE patientcorrelationdb.pddeferredcorrelation (
  Id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  MessageId VARCHAR(100) NOT NULL,
  AssigningAuthorityId varchar(64) NOT NULL,
  PatientId varchar(128) NOT NULL,
  CreationTime DATETIME NOT NULL,
  PRIMARY KEY (Id)
);
