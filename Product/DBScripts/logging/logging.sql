DROP TABLE IF EXISTS logging.log;
CREATE TABLE logging.log (
    dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    context varchar(100) DEFAULT NULL,
    logLevel varchar(10) DEFAULT NULL,
    class varchar(500) DEFAULT NULL,
    message longtext
);



