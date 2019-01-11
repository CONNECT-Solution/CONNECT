DROP DATABASE IF EXISTS adminguidb;
FLUSH PRIVILEGES;


-- begin adminguidb
CREATE DATABASE adminguidb;

-- -----------------------------------------------------
-- Table `adminguidb`.`UserLogin`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS adminguidb.UserRole (
	roleId SERIAL PRIMARY KEY,
	roleName VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS adminguidb.PagePreference (
	prefId SERIAL PRIMARY KEY,
	pageName VARCHAR(100) NOT NULL,
	pageDesc VARCHAR(100) NOT NULL,
	accessPage BIGINT NOT NULL,
	prefRoleId BIGINT unsigned NOT NULL,
	CONSTRAINT fk_role_pref
      FOREIGN KEY (prefRoleId)
      REFERENCES adminguidb.UserRole (roleId)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION	
);

CREATE TABLE IF NOT EXISTS adminguidb.UserLogin (
    id SERIAL PRIMARY KEY,
    salt varchar(100) NOT NULL,
    sha2 varchar(100) NOT NULL,
    userName varchar(100) NOT NULL UNIQUE,
	firstName varchar(100),
	middleName varchar(100),
	lastName varchar(100),
    userRole BIGINT unsigned NOT NULL,
	transactionUserRole varchar(100),
	transactionUserRoleDesc varchar(150),
    CONSTRAINT fk_role_user
        FOREIGN KEY (userRole)
        REFERENCES adminguidb.UserRole (roleId)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

INSERT INTO adminguidb.UserRole 
(roleId, roleName)
VALUES
(1, "ADMIN"),
(2, "SUPER USER"),
(3, "USER");

INSERT INTO adminguidb.PagePreference
(pageName, pageDesc, accessPage, prefRoleId)
VALUES
("acctmanage.xhtml", "Account Management", 0, 1),
("acctmanage.xhtml", "Account Management", -1, 2),
("acctmanage.xhtml", "Account Management", -1, 3),
("direct.xhtml", "Direct Config", 0, 1),
("direct.xhtml", "Direct Config", 0, 2),
("direct.xhtml", "Direct Config", 0, 3),
("exchangeManager.xhtml", "Exchange Management", 0, 1),
("exchangeManager.xhtml", "Exchange Management", 0, 2),
("exchangeManager.xhtml", "Exchange Management", 0, 3),
("properties.xhtml", "CONNECT Properties", 0, 1),
("properties.xhtml", "CONNECT Properties", 0, 2),
("properties.xhtml", "CONNECT Properties", 0, 3),
("fhir.xhtml", "FHIR Resources", 0, 1),
("fhir.xhtml", "FHIR Resources", 0, 2),
("fhir.xhtml", "FHIR Resources", 0, 3),
("patientDiscovery.xhtml", "Cross-Query Gateway Client", 0, 1),
("patientDiscovery.xhtml", "Cross-Query Gateway Client", 0, 2),
("patientDiscovery.xhtml", "Cross-Query Gateway Client", 0, 3),
("loadTestData.xhtml", "Test Data", 0, 1),
("loadTestData.xhtml", "Test Data", 0, 2),
("loadTestData.xhtml", "Test Data", 0, 3),
("auditLog.xhtml", "Logging", 0, 1),
("auditLog.xhtml", "Logging", 0, 2),
("auditLog.xhtml", "Logging", 0, 3),
("certificateManager.xhtml", "Certificate Management", 0, 1),
("certificateManager.xhtml", "Certificate Management", -1, 2),
("certificateManager.xhtml", "Certificate Management", -1, 3);

INSERT INTO adminguidb.UserLogin
(id, salt, sha2, userName, userRole)
VALUES
(1, "ABCD", "eFw9+D8egYfAGv1QjUMdVzI9dtvwiH3Amc6XlBoXZj03ebwzuQU8yoYzyLtz40JOn69a7P8zqtT7A6lEyIMBmw==", "CONNECTAdmin", 1);

GRANT SELECT,INSERT,UPDATE,DELETE ON adminguidb.* to nhincuser;
-- end adminguidb