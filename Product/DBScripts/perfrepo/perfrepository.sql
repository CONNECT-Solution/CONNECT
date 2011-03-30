
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `perfrepo` ;
CREATE SCHEMA IF NOT EXISTS `perfrepo` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `perfrepo` ;

-- -----------------------------------------------------
-- Table `perfrepo`.`perfrepository`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `perfrepo`.`perfrepository` ;

CREATE  TABLE IF NOT EXISTS `perfrepo`.`perfrepository` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `starttime` TIMESTAMP NULL ,
  `stoptime` TIMESTAMP NULL ,
  `duration` BIGINT NULL ,
  `servicetype` VARCHAR(45) NULL ,
  `messagetype` VARCHAR(10) NULL ,
  `direction` VARCHAR(10) NULL ,
  `communityid` VARCHAR(255) NULL ,
  `status` INT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB
COMMENT = 'Performance Monitor Repository';



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
