-- CONNECT 3.3
--
-- Increase all assigning authority ids and community ids to 64 character long
-- Created on: 2011-10-07
-- Author: ngoc.nguyen
-- ------------------------------------------------------


ALTER `assigningauthoritydb`.`aa_to_home_community_mapping` MODIFY assigningauthorityid VARCHAR(64);
ALTER `assigningauthoritydb`.`aa_to_home_community_mapping` MODIFY homecommunityid VARCHAR(64);
ALTER `auditrepo`.`auditrepository` MODIFY receiverPatientId VARCHAR(128);
ALTER `auditrepo`.`auditrepository` MODIFY senderPatientId VARCHAR(128);
ALTER `patientcorrelationdb`.`correlatedidentifiers` MODIFY PatientId VARCHAR(128);
ALTER `patientcorrelationdb`.`correlatedidentifiers` MODIFY PatientAssigningAuthorityId VARCHAR(64);
ALTER `patientcorrelationdb`.`correlatedidentifiers` MODIFY CorrelatedPatientId VARCHAR(128);
ALTER `patientcorrelationdb`.`correlatedidentifiers` MODIFY CorrelatedPatientAssignAuthId VARCHAR(64)
ALTER `patientcorrelationdb`.`pddeferredcorrelation` MODIFY AssigningAuthorityId VARCHAR(64);
ALTER `patientcorrelationdb`.`pddeferredcorrelation` MODIFY PatientId VARCHAR(128);;
ALTER `docrepository`.`document` MODIFY PatientId VARCHAR(128);
