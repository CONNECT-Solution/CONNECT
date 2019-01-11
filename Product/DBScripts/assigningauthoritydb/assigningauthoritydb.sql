--
-- Host: localhost    Database: assigningauthoritydb
-- ------------------------------------------------------
-- Server version	5.0.51b-community-nt\

--
-- Table structure for table `aa_to_home_community_mapping`
--

DROP TABLE IF EXISTS assigningauthoritydb.aa_to_home_community_mapping;
CREATE TABLE assigningauthoritydb.aa_to_home_community_mapping (
  id int(10) unsigned NOT NULL auto_increment,
  assigningauthorityid varchar(64) NOT NULL,
  homecommunityid varchar(64) NOT NULL,
  PRIMARY KEY  (id,assigningauthorityid)
);