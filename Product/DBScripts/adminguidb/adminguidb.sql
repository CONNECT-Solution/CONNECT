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
    sha1 varchar(100) NOT NULL,
    userName varchar(100) NOT NULL UNIQUE,
    userRole BIGINT unsigned NOT NULL,
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
("connectionManager.xhtml", "Connection Management", 0, 1),
("connectionManager.xhtml", "Connection Management", 0, 2),
("connectionManager.xhtml", "Connection Management", 0, 3),
("properties.xhtml", "CONNECT Properties", 0, 1),
("properties.xhtml", "CONNECT Properties", 0, 2),
("properties.xhtml", "CONNECT Properties", 0, 3);


INSERT INTO adminguidb.UserLogin
(id, salt, sha1, userName, userRole)
VALUES
(1, "ABCD", "TxMu4SPUdek0XU5NovS9U2llt3Q=", "CONNECTAdmin", 1);

GRANT SELECT,INSERT,UPDATE,DELETE ON adminguidb.* to nhincuser;
-- end adminguidb