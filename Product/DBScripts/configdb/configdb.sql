-- begin configdb
CREATE DATABASE configdb;

-- -----------------------------------------------------
-- Table `configdb`.`domain`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.domain (
    id SERIAL PRIMARY KEY,
    domainName VARCHAR(255) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    createTime DATETIME NOT NULL,
    updateTime DATETIME
);

-- -----------------------------------------------------
-- Table `configdb`.`address`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.address (
    id SERIAL PRIMARY KEY,
    eMailAddress VARCHAR(255) NOT NULL,
    displayName VARCHAR(100),
    endpoint VARCHAR(255),
    type VARCHAR(4),
    status BOOLEAN NOT NULL DEFAULT TRUE,
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
    forIncoming BOOLEAN NOT NULL DEFAULT TRUE,
    forOutgoing BOOLEAN NOT NULL DEFAULT TRUE,
    status BOOLEAN NOT NULL DEFAULT TRUE,
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
    privateKey SMALLINT,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    createTime DATETIME NOT NULL
);

-- -----------------------------------------------------
-- Table `configdb`.`setting`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS configdb.setting (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    value VARCHAR(4096),
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
    forIncoming BOOLEAN NOT NULL DEFAULT TRUE,
    forOutgoing BOOLEAN NOT NULL DEFAULT TRUE,

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

GRANT SELECT,INSERT,UPDATE,DELETE ON configdb.* to nhincuser;
-- end configdb
