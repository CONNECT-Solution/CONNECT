
-- -----------------------------------------------------
-- Table `perfrepo`.`perfrepository`
-- -----------------------------------------------------
DROP TABLE IF EXISTS perfrepo.perfrepository;

CREATE TABLE perfrepo.perfrepository (
  id BIGINT NOT NULL AUTO_INCREMENT,
  starttime TIMESTAMP NULL,
  stoptime TIMESTAMP NULL,
  duration BIGINT NULL,
  servicetype VARCHAR(45) NULL,
  messagetype VARCHAR(10) NULL,
  direction VARCHAR(10) NULL,
  communityid VARCHAR(255) NULL,
  status INT NULL DEFAULT 0,
  version VARCHAR(10),
  size VARCHAR(10),
  payloadtype VARCHAR(10),
  correlationid VARCHAR(255),
  othercommunityid VARCHAR(255),
  errorcode VARCHAR(10),
 PRIMARY KEY (id),
  UNIQUE INDEX id_UNIQUE (id ASC) )
COMMENT = 'Performance Monitor Repository';

