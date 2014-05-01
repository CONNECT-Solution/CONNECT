-- begin adminguidb
CREATE DATABASE adminguidb;

-- -----------------------------------------------------
-- Table `adminguidb`.`UserLogin`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS adminguidb.UserLogin (
    id SERIAL PRIMARY KEY,
    salt varchar(100) NOT NULL,
    sha1 varchar(100) NOT NULL,
    userName varchar(100) NOT NULL UNIQUE
);

INSERT INTO adminguidb.UserLogin
(id, salt, sha1, userName)
VALUES
(1, "ABCD", "TxMu4SPUdek0XU5NovS9U2llt3Q=", "CONNECTAdmin");

GRANT SELECT,INSERT,UPDATE,DELETE ON adminguidb.* to nhincuser;
-- end adminguidb
