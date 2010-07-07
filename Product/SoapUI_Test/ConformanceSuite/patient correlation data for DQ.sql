-- MySQL Patient Correlation dump for Doc Query Conformance Tests
--
-- Host: localhost   Database: patientcorrelationdb
-- ------------------------------------------------------

LOCK TABLES patientcorrelationdb.correlatedidentifiers WRITE;

DELETE from patientcorrelationdb.correlatedidentifiers;

INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('1','1.1','0000002009','2.2','0000002009',null);
INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('2','1.1','0000002007','2.2','0000002007',null);
INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('3','1.1','0000002022','2.2','0000002022',null);
INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('4','1.1','0000002008','2.2','0000002008',null);
INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('5','1.1','0000002017','2.2','0000002017',null);
INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('6','1.1','0000002023','2.2','0000002023',null);
INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('7','1.1','0000002025','2.2','0000002025',null);
INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('8','1.1','0000002021','2.2','0000002021',null);
INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('9','1.1','0000002020','2.2','0000002020',null);
INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('10','1.1','N600011','2.2','N600011',null);
INSERT INTO patientcorrelationdb.correlatedidentifiers VALUES ('11','1.1','N999999','2.2','N999999',null);