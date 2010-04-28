
--
-- Change the name of the table.
--
alter table assigningauthoritydb.aa_to_home_community_mapping rename to assigningauthoritydb.assigningauthoritytohomecommunitymapping;

--
-- Change the column names of columns in existing tables.
--

alter table docrepository.document change column DocumentSize Size int(11) default NULL;

alter table auditrepo.auditrepository change column audit_timestamp timestamp DATETIME;
