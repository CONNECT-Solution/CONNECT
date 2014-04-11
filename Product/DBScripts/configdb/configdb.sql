-- begin configdb
CREATE DATABASE configdb;

-- -----------------------------------------------------
-- Table `configdb`.`domain`
-- -----------------------------------------------------

CREATE TABLE configdb.domain (
    id SERIAL PRIMARY KEY,
    domainName VARCHAR(255) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    createTime DATETIME NOT NULL,
    updateTime DATETIME
);

-- -----------------------------------------------------
-- Table `configdb`.`endpointType`
-- -----------------------------------------------------

CREATE TABLE configdb.endpointType (
    id SERIAL PRIMARY KEY,
    name VARCHAR(4) NOT NULL
);

INSERT INTO configdb.endpointType (name) VALUES ('SMTP'), ('XDR'), ('XDM');

-- -----------------------------------------------------
-- Table `configdb`.`address`
-- -----------------------------------------------------

CREATE TABLE configdb.address (
    id SERIAL PRIMARY KEY,
    domainId BIGINT NOT NULL REFERENCES configdb.domain(id),
    eMailAddress VARCHAR(255) NOT NULL,
    displayName VARCHAR(100),
    endpoint VARCHAR(255),
    type BIGINT REFERENCES configdb.endpointType(id),
    status BOOLEAN NOT NULL DEFAULT TRUE,
    createTime DATETIME NOT NULL,
    updateTime DATETIME
);

-- -----------------------------------------------------
-- Table `configdb`.`anchor`
-- -----------------------------------------------------

CREATE TABLE configdb.anchor (
    id SERIAL PRIMARY KEY,
    certificateId BIGINT NOT NULL COMMENT '?',
    owner VARCHAR(255) NOT NULL COMMENT 'Subject CN',
    thumbprint VARCHAR(64) NOT NULL,
    certificateData BLOB(4096) NOT NULL,
    validStartDate DATETIME NOT NULL,
    validEndDate DATETIME NOT NULL,
    forIncoming BOOLEAN NOT NULL DEFAULT TRUE,
    forOutgoing BOOLEAN NOT NULL DEFAULT TRUE,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    createTime DATETIME NOT NULL
);

-- -----------------------------------------------------
-- Table `configdb`.`certificate`
-- -----------------------------------------------------

CREATE TABLE configdb.certificate (
    id SERIAL PRIMARY KEY,
    owner VARCHAR(255) NOT NULL COMMENT 'Subject CN',
    thumbprint VARCHAR(64) NOT NULL,
    certificateData BLOB(4096) NOT NULL,
    validStartDate DATETIME NOT NULL,
    validEndDate DATETIME NOT NULL,
    privateKey SMALLINT,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    createTime DATETIME NOT NULL
);

-- -----------------------------------------------------
-- Table `configdb`.`setting`
-- -----------------------------------------------------

CREATE TABLE configdb.setting (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    value VARCHAR(4096),
    createTime DATETIME NOT NULL,
    updateTime DATETIME
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundle`
-- -----------------------------------------------------

CREATE TABLE configdb.trustbundle (
    id SERIAL PRIMARY KEY,
    bundleName VARCHAR(255) NOT NULL,
    bundleURL VARCHAR(255) NOT NULL,
    getCheckSum VARCHAR(255) NOT NULL,
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

CREATE TABLE configdb.trustbundleanchor (
    id SERIAL PRIMARY KEY,
    trustbundleId BIGINT NOT NULL REFERENCES configdb.trustbundle(id),
    anchorData BLOB(4096) NOT NULL,
    thumbprint VARCHAR(64) NOT NULL,
    validStartDate DATETIME NOT NULL,
    validEndDate DATETIME NOT NULL
);

-- -----------------------------------------------------
-- Table `configdb`.`trustbundledomainreltn`
-- -----------------------------------------------------

CREATE TABLE configdb.trustbundledomainreltn (
    id SERIAL PRIMARY KEY,
    domain_id BIGINT NOT NULL REFERENCES configdb.domain (id),
    trust_bundle_id BIGINT NOT NULL REFERENCES configdb.trustbundle(id),
    forIncoming BOOLEAN NOT NULL DEFAULT TRUE,
    forOutgoing BOOLEAN NOT NULL DEFAULT TRUE
);

GRANT SELECT,INSERT,UPDATE,DELETE ON configdb.* to nhincuser;
-- end configdb
