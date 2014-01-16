-- begin configdb
CREATE DATABASE configdb;

-- -----------------------------------------------------
-- Table `configdb`.`domain`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.domain
CREATE TABLE configdb.domain (
    id serial PRIMARY KEY,
    createTime TIMESTAMP, domainname VARCHAR(255),
    postmasterAddressId BIGINT,
    status INTEGER,
    updateTime TIMESTAMP
);

CREATE UNIQUE INDEX domain_name_idx ON configdb.domain (domainName);

-- -----------------------------------------------------
-- Table `configdb`.`address`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.address
CREATE TABLE configdb.address (
    id serial PRIMARY KEY,
    displayName varchar(100),
    eMailAddress varchar(255),
    endpoint varchar(255),
    status smallint DEFAULT 0,
    type varchar(64),
    createTime TIMESTAMP DEFAULT NOW(),
    updateTime TIMESTAMP,
    domainId bigint NOT NULL references configdb.domain(id)
);

CREATE UNIQUE INDEX address_email_idx ON configdb.address (eMailAddress(255));

-- -----------------------------------------------------
-- Table `configdb`.`anchor`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.anchor
CREATE TABLE configdb.anchor (
    id serial PRIMARY KEY,
    owner varchar(255),
    thumbprint varchar(64),
    certificateId bigint,
    createTime TIMESTAMP DEFAULT NOW(),
    certificateData mediumblob,
    validStartDate timestamp,
    validEndDate timestamp,
    forIncoming smallint DEFAULT 1,
    forOutgoing smallint DEFAULT 1,
    status smallint DEFAULT 0
);

CREATE UNIQUE INDEX anchor_owner_tprint_idx ON configdb.anchor (owner(255), thumbprint);

-- -----------------------------------------------------
-- Table `configdb`.`certificate`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.certificate
CREATE TABLE configdb.certificate (
    id serial PRIMARY KEY,
    owner varchar(255),
    thumbprint varchar(64),
    createTime TIMESTAMP DEFAULT NOW(),
    certificateData mediumblob,
    validStartDate timestamp,
    validEndDate timestamp,
    status smallint DEFAULT 0,
    PRIVATEKEY SMALLINT
);

CREATE UNIQUE INDEX certificate_owner_tprint_idx ON configdb.certificate (owner(255), thumbprint);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicy`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.certpolicy
CREATE TABLE configdb.certpolicy (
    id serial PRIMARY KEY,
    createtime TIMESTAMP NOT NULL,
    lexicon INTEGER NOT NULL,
    data BLOB(204800) NOT NULL,
    policyName VARCHAR(255)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroup`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.certpolicygroup
CREATE TABLE configdb.certpolicygroup (
    id serial PRIMARY KEY,
    createTime TIMESTAMP NOT NULL,
    policyGroupName VARCHAR(255)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroupdomainreltn`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.certpolicygroupdomainreltn
CREATE TABLE configdb.certpolicygroupdomainreltn (
    id serial PRIMARY KEY,
    policy_group_id BIGINT NOT NULL references configdb.domain(id),
    domain_id BIGINT NOT NULL references configdb.certpolicygroup(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroupreltn`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.certpolicygroupreltn
CREATE TABLE configdb.certpolicygroupreltn (
    id serial PRIMARY KEY,
    incoming SMALLINT,
    outgoing SMALLINT,
    policyUse INTEGER NOT NULL,
    certPolicyId BIGINT NOT NULL references configdb.certpolicy(id),
    certPolicyGroupId BIGINT NOT NULL references configdb.certpolicygroup(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`dnsrecord`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.dnsrecord
CREATE TABLE configdb.dnsrecord (
    id serial PRIMARY KEY,
    createTime TIMESTAMP,
    data BLOB(8192),
    dclass INTEGER,
    name VARCHAR(255),
    ttl BIGINT,
    type INTEGER
);

-- -----------------------------------------------------
-- Table `configdb`.`setting`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.setting
CREATE TABLE configdb.setting (
    id serial PRIMARY KEY,
    name VARCHAR(255),
    status INTEGER,
    createTime TIMESTAMP,
    updateTime TIMESTAMP,
    value VARCHAR(4096)
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundle`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.trustbundle
CREATE TABLE configdb.trustbundle (
    id serial PRIMARY KEY,
    bundleName VARCHAR(255) NOT NULL,
    bundleURL VARCHAR(255) NOT NULL,
    getCheckSum VARCHAR(255) NOT NULL,
    createTime TIMESTAMP NOT NULL,
    lastRefreshAttempt TIMESTAMP,
    lastRefreshError INTEGER,
    lastSuccessfulRefresh TIMESTAMP,
    refreshInterval INTEGER,
    signingCertificateData BLOB(4096)
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundleanchor`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.trustbundleanchor
CREATE TABLE configdb.trustbundleanchor (
    id serial PRIMARY KEY,
    anchorData BLOB(4096) NOT NULL,
    thumbprint VARCHAR(255) NOT NULL,
    validEndDate TIMESTAMP NOT NULL,
    validStartDate TIMESTAMP NOT NULL,
    trustbundleId BIGINT NOT NULL references configdb.trustbundle(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundledomainreltn`
-- -----------------------------------------------------

DROP TABLE IF EXISTS configdb.trustbundledomainreltn
CREATE TABLE configdb.trustbundledomainreltn (
    id serial PRIMARY KEY,
    forIncoming SMALLINT,
    forOutgoing SMALLINT,
    domain_id BIGINT NOT NULL references configdb.domain (id),
    trust_bundle_id BIGINT NOT NULL references configdb.trustbundle(id)
);

-- CREATE TABLE configdb.HIBERNATE_UNIQUE_KEY (
--     NEXT_HI INTEGER
-- );

GRANT SELECT,INSERT,UPDATE,DELETE ON configdb.* to nhincuser;

GRANT SELECT,INSERT,UPDATE,DELETE ON *.* TO 'nhincuser'@'localhost' IdENTIFIED BY 'nhincpass' WITH GRANT OPTION;
GRANT SELECT,INSERT,UPDATE,DELETE ON *.* TO 'nhincuser'@'127.0.0.1' IdENTIFIED BY 'nhincpass' WITH GRANT OPTION;