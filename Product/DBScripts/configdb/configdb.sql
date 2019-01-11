-- begin configdb
CREATE DATABASE configdb;

-- -----------------------------------------------------
-- Table `configdb`.`domain`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.domain (
    id SERIAL PRIMARY KEY,
    postmasterAddressId BIGINT,
    domainName VARCHAR(255) NOT NULL UNIQUE,
    status INTEGER DEFAULT 0,
    createTime DATETIME NOT NULL,
    updateTime DATETIME
);

-- -----------------------------------------------------
-- Table `configdb`.`address`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.address (
    id SERIAL PRIMARY KEY,
    emailAddress VARCHAR(255) NOT NULL,
    displayName VARCHAR(100),
    endpoint VARCHAR(255),
    type VARCHAR(4),
    status INTEGER DEFAULT 0,
    createTime DATETIME NOT NULL,
    updateTime DATETIME,

    domainId BIGINT UNSIGNED NOT NULL,
    INDEX fk_domainId (domainId ASC),
    CONSTRAINT fk_domainId
        FOREIGN KEY (domainId)
        REFERENCES configdb.domain(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table `configdb`.`anchor`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.anchor (
    id SERIAL PRIMARY KEY,
    certificateId BIGINT NOT NULL COMMENT '?',
    owner VARCHAR(255) NOT NULL COMMENT 'Subject CN',
    thumbprint VARCHAR(64) NOT NULL,
    certificateData BLOB(4096) NOT NULL,
    validStartDate DATETIME NOT NULL,
    validEndDate DATETIME NOT NULL,
    incoming BOOLEAN NOT NULL DEFAULT TRUE,
    outgoing BOOLEAN NOT NULL DEFAULT TRUE,
    status INTEGER DEFAULT 0,
    createTime DATETIME NOT NULL
);

-- -----------------------------------------------------
-- Table `configdb`.`certificate`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.certificate (
    id SERIAL PRIMARY KEY,
    owner VARCHAR(255) NOT NULL COMMENT 'Subject CN',
    thumbprint VARCHAR(64) NOT NULL,
    certificateData BLOB(4096) NOT NULL,
    validStartDate DATETIME NOT NULL,
    validEndDate DATETIME NOT NULL,
    privateKey BOOLEAN NOT NULL DEFAULT FALSE,
    status INTEGER DEFAULT 0,
    createTime DATETIME NOT NULL
);

-- -----------------------------------------------------
-- Table `configdb`.`setting`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.setting (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    value VARCHAR(4096),
    status INTEGER DEFAULT 0,
    createTime DATETIME NOT NULL,
    updateTime DATETIME
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundle`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.trustbundle (
    id SERIAL PRIMARY KEY,
    bundleName VARCHAR(255) NOT NULL,
    bundleURL VARCHAR(255) NOT NULL,
    bundleChecksum VARCHAR(255) NOT NULL,
    lastRefreshAttempt DATETIME,
    lastSuccessfulRefresh DATETIME,
    refreshInterval INTEGER,
    lastRefreshError INTEGER COMMENT 'enum value for refresh status message',
    signingCertificateData BLOB(4096),
    createTime DATETIME NOT NULL
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundleanchor`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.trustbundleanchor (
    id SERIAL PRIMARY KEY,
    anchorData BLOB(4096) NOT NULL,
    thumbprint VARCHAR(64) NOT NULL,
    validStartDate DATETIME NOT NULL,
    validEndDate DATETIME NOT NULL,

    trustbundleId BIGINT UNSIGNED NOT NULL,
    INDEX fk_trustbundleId (trustbundleId ASC),
    CONSTRAINT fk_trustbundleId
        FOREIGN KEY (trustbundleId)
        REFERENCES configdb.trustbundle(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundledomainreltn`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.trustbundledomainreltn (
    id SERIAL PRIMARY KEY,
    incoming BOOLEAN NOT NULL DEFAULT TRUE,
    outgoing BOOLEAN NOT NULL DEFAULT TRUE,

    domain_id BIGINT UNSIGNED NOT NULL REFERENCES configdb.domain (id),
    INDEX fk_domain_id (domain_id ASC),
    CONSTRAINT fk_domain_id
        FOREIGN KEY (domain_id)
        REFERENCES configdb.domain(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,

    trust_bundle_id BIGINT UNSIGNED NOT NULL REFERENCES configdb.trustbundle(id),
    INDEX fk_trust_bundle_id (trust_bundle_id ASC),
    CONSTRAINT fk_trust_bundle_id
        FOREIGN KEY (trust_bundle_id)
        REFERENCES configdb.trustbundle(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicy`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.certpolicy (
    id SERIAL PRIMARY KEY,
    createTime DATETIME NOT NULL,
    lexicon INTEGER NOT NULL,
    policyData BLOB(204800) NOT NULL,
    policyName VARCHAR(255)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroup`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.certpolicygroup (
    id SERIAL PRIMARY KEY,
    createTime DATETIME NOT NULL,
    policyGroupName VARCHAR(255)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroupdomainreltn`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.certpolicygroupdomainreltn (
    id SERIAL PRIMARY KEY,
    policy_group_id BIGINT NOT NULL REFERENCES configdb.certpolicygroup(id),
    domain_id BIGINT NOT NULL REFERENCES configdb.domain(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroupreltn`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.certpolicygroupreltn (
    id SERIAL PRIMARY KEY,
    incoming SMALLINT,
    outgoing SMALLINT,
    policyUse INTEGER NOT NULL,
    certPolicyId BIGINT NOT NULL REFERENCES configdb.certpolicy(id),
    certPolicyGroupId BIGINT NOT NULL REFERENCES configdb.certpolicygroup(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`dnsrecord`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.dnsrecord (
    id SERIAL PRIMARY KEY,
    createTime DATETIME NOT NULL,
    data BLOB(8192),
    dclass INTEGER,
    name VARCHAR(255),
    ttl BIGINT,
    type INTEGER
);

GRANT SELECT,INSERT,UPDATE,DELETE ON configdb.* to nhincuser;
-- end configdb

-- -----------------------------------------------------
-- The following is a workaround that is required for
-- deployment due to a bug in Direct RI
-- -----------------------------------------------------
USE configdb;
LOCK TABLES domain WRITE, address WRITE;

INSERT INTO domain
(id, domainName, postmasterAddressId, status, createTime, updateTime)
VALUES
(1, 'direct.example.org', NULL, 1, now(), now());

INSERT INTO address
(id, displayName, emailAddress, endpoint, status, type, createTime, updateTime, domainId)
VALUES
(1, 'direct.example.org', 'postmaster@direct.example.org', NULL, 1, NULL, now(), now(), 1);

UPDATE address SET id = 2 WHERE id = 1;

UPDATE domain SET postmasterAddressId = 2 WHERE id = 1;

UNLOCK TABLES;
