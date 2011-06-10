-- CONNECT 3.2
--
-- Default Document Repository - Migrate Patient Ids
-- Created on: 2011-06-09
-- Author: richard.ettema
-- ------------------------------------------------------


-- Update all SourcePatientId values with Local Assigning Authority Id where SourcePatientId equials PatientID
-- PatientId column stores all local patient ids
--
UPDATE `docrepository`.`document` SET SourcePatientId = CONCAT(PatientId,'^^^\&[LOCAL AA ID]\&ISO') WHERE SourcePatientId = PatientId;

UPDATE `docrepository`.`document` SET PatientId = CONCAT(PatientId,'^^^\&[LOCAL AA ID]\&ISO');
