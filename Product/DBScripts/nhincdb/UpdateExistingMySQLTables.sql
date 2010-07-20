
--
-- Change the name of the table.
--
alter table assigningauthoritydb.assigningauthoritytohomecommunitymapping rename to assigningauthoritydb.aa_to_home_community_mapping;

--
-- Change the column names of columns in existing tables.
--

alter table docrepository.document change column Size DocumentSize int(11) default NULL;

alter table auditrepo.auditrepository change column timestamp audit_timestamp DATETIME;
