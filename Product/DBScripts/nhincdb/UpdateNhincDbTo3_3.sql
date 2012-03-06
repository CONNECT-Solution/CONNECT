-- CONNECT 3.3
--
-- Increase all assigning authority ids and community ids to 64 character long
-- Drop database lift
-- Add On-Demand columns
--
-- Created on: 2011-10-07
-- Author: ngoc.nguyen
-- ------------------------------------------------------

-- Update IDs length

ALTER TABLE `assigningauthoritydb`.`aa_to_home_community_mapping` MODIFY assigningauthorityid VARCHAR(64) NOT NULL;
ALTER TABLE `assigningauthoritydb`.`aa_to_home_community_mapping` MODIFY homecommunityid VARCHAR(64) NOT NULL;
ALTER TABLE `auditrepo`.`auditrepository` MODIFY receiverPatientId VARCHAR(128);
ALTER TABLE `auditrepo`.`auditrepository` MODIFY senderPatientId VARCHAR(128);
ALTER TABLE `patientcorrelationdb`.`correlatedidentifiers` MODIFY PatientId VARCHAR(128) NOT NULL;
ALTER TABLE `patientcorrelationdb`.`correlatedidentifiers` MODIFY PatientAssigningAuthorityId VARCHAR(64) NOT NULL;
ALTER TABLE `patientcorrelationdb`.`correlatedidentifiers` MODIFY CorrelatedPatientId VARCHAR(128) NOT NULL;
ALTER TABLE `patientcorrelationdb`.`correlatedidentifiers` MODIFY CorrelatedPatientAssignAuthId VARCHAR(64) NOT NULL;
ALTER TABLE `patientcorrelationdb`.`pddeferredcorrelation` MODIFY AssigningAuthorityId VARCHAR(64) NOT NULL;
ALTER TABLE `patientcorrelationdb`.`pddeferredcorrelation` MODIFY PatientId VARCHAR(128) NOT NULL;
ALTER TABLE `docrepository`.`document` MODIFY PatientId VARCHAR(128) default NULL COMMENT 'Format of HL7 2.x CX';

-- Drop lift Database

drop database lift;

-- Add On-Demand columns

ALTER TABLE `docrepository`.`document` ADD OnDemand tinyint(1) NOT NULL default 0 COMMENT 'Indicate whether document is dynamic (true or 1) or static (false or 0).';
ALTER TABLE `docrepository`.`document` ADD NewDocumentUniqueId varchar(128) default NULL;
ALTER TABLE `docrepository`.`document` ADD NewRepositoryUniqueId varchar(128) default NULL;