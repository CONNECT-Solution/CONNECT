-- -------------------------------------------------------------------------------------------------------
-- --------This needs testing on Oracle Instance
-- -------------------------------------------------------------------------------------------------------

-- -------------------------------------------------------------------------------------------------------
-- ALTER TABLE AuditRepository
-- -------------------------------------------------------------------------------------------------------

ALTER TABLE "NHINCUSER"."AUDITREPOSITORY" 
 ADD COLUMN eventType VARCHAR2(100 BYTE) NOT NULL AFTER userId,
 ADD COLUMN outcome NUMBER(3,0) NOT NULL AFTER eventType,
 ADD COLUMN messageId VARCHAR2(100 BYTE) DEFAULT NULL AFTER outcome,
 ADD COLUMN relatesTo VARCHAR2(100 BYTE) DEFAULT NULL AFTER messageId,
 ADD COLUMN direction VARCHAR2(20 BYTE) NOT NULL AFTER relatesTo;

Alter TABLE "NHINCUSER"."AUDITREPOSITORY" 
 DROP COLUMN participationTypeCode,
 DROP COLUMN participationTypeCodeRole,
 DROP COLUMN participationIDTypeCode,
 DROP COLUMN receiverPatientId,
 DROP COLUMN senderPatientId,
 DROP COLUMN messageType;
 
ALTER TABLE "NHINCUSER"."AUDITREPOSITORY" 
 RENAME COLUMN communityId remoteHcid DEFAULT NULL VARCHAR(255 BYTE),
 RENAME COLUMN audit_timestamp eventTimestamp DATE NOT NULL;
 
ALTER TABLE "NHINCUSER"."AUDITREPOSITORY" MODIFY COLUMN eventId VARCHAR(100 BYTE) NOT NULL; 

ALTER TABLE "NHINCUSER"."AUDITREPOSITORY"  MODIFY COLUMN message BLOB NOT NULL;