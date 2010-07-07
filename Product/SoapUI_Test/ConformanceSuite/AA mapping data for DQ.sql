-- MySQL Assigning Authority mapping dump for Doc Query Conformance Tests
--
-- Host: localhost   Database: assigningauthoritydb
-- ------------------------------------------------------

LOCK TABLES assigningauthoritydb.aa_to_home_community_mapping WRITE;

DELETE FROM assigningauthoritydb.aa_to_home_community_mapping;

INSERT INTO assigningauthoritydb.aa_to_home_community_mapping VALUES ('1','1.1','1.1');
INSERT INTO assigningauthoritydb.aa_to_home_community_mapping VALUES ('2','2.2','2.2');
