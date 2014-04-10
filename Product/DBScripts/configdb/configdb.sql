-- begin configdb
CREATE DATABASE configdb;

-- -----------------------------------------------------
-- Table `configdb`.`domain`
-- -----------------------------------------------------

CREATE TABLE configdb.domain (
    id SERIAL PRIMARY KEY,
    domainName VARCHAR(255),
    createTime DATETIME,
    postmasterAddressId BIGINT REFERENCES configdb.address(id),
    status SMALLINT DEFAULT 0,
    updateTime DATETIME
);

-- -----------------------------------------------------
-- Table `configdb`.`address`
-- -----------------------------------------------------

CREATE TABLE configdb.address (
    id SERIAL PRIMARY KEY,
    displayName VARCHAR(100),
    eMailAddress VARCHAR(255),
    endpoint VARCHAR(255),
    status SMALLINT DEFAULT 0,
    type VARCHAR(64),
    createTime DATETIME,
    updateTime DATETIME,
    domainId BIGINT NOT NULL REFERENCES configdb.domain(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`anchor`
-- -----------------------------------------------------

CREATE TABLE configdb.anchor (
    id SERIAL PRIMARY KEY,
    owner VARCHAR(255),
    thumbprint VARCHAR(64),
    certificateId BIGINT,
    createTime DATETIME,
    certificateData MEDIUMBLOB,
    validStartDate DATETIME,
    validEndDate DATETIME,
    forIncoming SMALLINT DEFAULT 1,
    forOutgoing SMALLINT DEFAULT 1,
    status SMALLINT DEFAULT 0
);

-- -----------------------------------------------------
-- Table `configdb`.`certificate`
-- -----------------------------------------------------

CREATE TABLE configdb.certificate (
    id SERIAL PRIMARY KEY,
    owner VARCHAR(255),
    thumbprint VARCHAR(64),
    createTime DATETIME,
    certificateData MEDIUMBLOB,
    validStartDate DATETIME,
    validEndDate DATETIME,
    status SMALLINT DEFAULT 0,
    privateKey SMALLINT
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicy`
-- -----------------------------------------------------

CREATE TABLE configdb.certpolicy (
    id SERIAL PRIMARY KEY,
    createTime DATETIME NOT NULL,
    lexicon INTEGER NOT NULL,
    data BLOB(204800) NOT NULL,
    policyName VARCHAR(255)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroup`
-- -----------------------------------------------------

CREATE TABLE configdb.certpolicygroup (
    id SERIAL PRIMARY KEY,
    createTime DATETIME NOT NULL,
    policyGroupName VARCHAR(255)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroupdomainreltn`
-- -----------------------------------------------------

CREATE TABLE configdb.certpolicygroupdomainreltn (
    id SERIAL PRIMARY KEY,
    policy_group_id BIGINT NOT NULL REFERENCES configdb.domain(id),
    domain_id BIGINT NOT NULL REFERENCES configdb.certpolicygroup(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`certpolicygroupreltn`
-- -----------------------------------------------------

CREATE TABLE configdb.certpolicygroupreltn (
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

CREATE TABLE configdb.dnsrecord (
    id SERIAL PRIMARY KEY,
    createTime DATETIME,
    data BLOB(8192),
    dclass INTEGER,
    name VARCHAR(255),
    ttl BIGINT,
    type INTEGER
);

-- -----------------------------------------------------
-- Table `configdb`.`setting`
-- -----------------------------------------------------

CREATE TABLE configdb.setting (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    status INTEGER,
    createTime DATETIME,
    updateTime DATETIME,
    value VARCHAR(4096)
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundle`
-- -----------------------------------------------------

CREATE TABLE configdb.trustbundle (
    id SERIAL PRIMARY KEY,
    bundleName VARCHAR(255) NOT NULL,
    bundleURL VARCHAR(255) NOT NULL,
    getCheckSum VARCHAR(255) NOT NULL,
    createTime DATETIME NOT NULL,
    lastRefreshAttempt DATETIME,
    lastRefreshError INTEGER,
    lastSuccessfulRefresh DATETIME,
    refreshInterval INTEGER,
    signingCertificateData BLOB(4096)
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundleanchor`
-- -----------------------------------------------------

CREATE TABLE configdb.trustbundleanchor (
    id SERIAL PRIMARY KEY,
    anchorData BLOB(4096) NOT NULL,
    thumbprint VARCHAR(255) NOT NULL,
    validEndDate DATETIME NOT NULL,
    validStartDate DATETIME NOT NULL,
    trustbundleId BIGINT NOT NULL REFERENCES configdb.trustbundle(id)
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundledomainreltn`
-- -----------------------------------------------------

CREATE TABLE configdb.trustbundledomainreltn (
    id SERIAL PRIMARY KEY,
    forIncoming SMALLINT,
    forOutgoing SMALLINT,
    domain_id BIGINT NOT NULL REFERENCES configdb.domain (id),
    trust_bundle_id BIGINT NOT NULL REFERENCES configdb.trustbundle(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON configdb.* to nhincuser;
-- end configdb