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
	access BIGINT NOT NULL,
	roleId BIGINT NOT NULL,
	CONSTRAINT fk_role_pref
      FOREIGN KEY (roleId)
      REFERENCES adminguidb.UserRole (id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION	
);

CREATE TABLE IF NOT EXISTS adminguidb.UserLogin (
    id SERIAL PRIMARY KEY,
    salt varchar(100) NOT NULL,
    sha1 varchar(100) NOT NULL,
    userName varchar(100) NOT NULL UNIQUE,
    userRole BIGINT NOT NULL,
    CONSTRAINT fk_role_user
      FOREIGN KEY (userRole)
      REFERENCES adminguidb.UserRole (id)
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
(pageName, pageDesc, access, roleId)
VALUES
("status.xhtml", "Status", 0, 1),
("status.xhtml", "Status", 0, 2),
("status.xhtml", "Status", 0, 3),
("StatusPrime.xhtml", "Status Prime", 0, 1),
("StatusPrime.xhtml", "Status Prime", 0, 2),
("StatusPrime.xhtml", "Status Prime", 0, 3),
("acctmanage.xhtml", "Account Management", 0, 1),
("acctmanage.xhtml", "Account Management", -1, 2),
("acctmanage.xhtml", "Account Management", -1, 3),
("acctmanagePrime.xhtml", "Account Management Prime", 0, 1),
("acctmanagePrime.xhtml", "Account Management Prime", -1, 2),
("acctmanagePrime.xhtml", "Account Management Prime", -1, 3);

INSERT INTO adminguidb.UserLogin
(id, salt, sha1, userName, userRole)
VALUES
(1, "ABCD", "TxMu4SPUdek0XU5NovS9U2llt3Q=", "CONNECTAdmin", 1);

GRANT SELECT,INSERT,UPDATE,DELETE ON adminguidb.* to nhincuser;
-- end adminguidb