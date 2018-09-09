-- MySQL Script generated by MySQL Workbench
-- Mon 14 May 2018 10:05:49 PM EEST
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema web_dev_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema web_dev_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `web_dev_db` DEFAULT CHARACTER SET utf8 ;
USE `web_dev_db` ;

-- -----------------------------------------------------
-- Table `web_dev_db`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`User` (
  `user_id` INT NOT NULL,
  `user_email` VARCHAR(45) NULL,
  `user_password` VARCHAR(45) NULL,
  `user_name` VARCHAR(45) NULL,
  `user_surname` VARCHAR(45) NULL,
  `user_phone_number` VARCHAR(45) NULL,
  `user_profile_picture` BLOB(100) NULL,
  `user_city` VARCHAR(45) NULL,
  `user_profession` VARCHAR(45) NULL,
  `user_company` VARCHAR(45) NULL,
  `is_admin` TINYINT(1) NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `web_dev_db`.`UserNetwork`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`UserNetwork` (
  `Users_user_id` INT NOT NULL,
  `Users_user_id1` INT NOT NULL,
  PRIMARY KEY (`Users_user_id`, `Users_user_id1`),
  INDEX `fk_Users_has_Users_Users1_idx` (`Users_user_id1` ASC),
  INDEX `fk_Users_has_Users_Users_idx` (`Users_user_id` ASC),
  CONSTRAINT `fk_Users_has_Users_Users`
    FOREIGN KEY (`Users_user_id`)
    REFERENCES `web_dev_db`.`User` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Users_has_Users_Users1`
    FOREIGN KEY (`Users_user_id1`)
    REFERENCES `web_dev_db`.`User` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `web_dev_db`.`Post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`Post` (
  `post_id` INT NOT NULL,
  `post_content` TINYTEXT NULL,
  `post_timestamp` TIMESTAMP(1) NULL,
  `is_advertisment` TINYINT(1) NULL,
  `User_user_id` INT NOT NULL,
  PRIMARY KEY (`post_id`, `User_user_id`),
  INDEX `fk_Post_User1_idx` (`User_user_id` ASC),
  CONSTRAINT `fk_Post_User1`
    FOREIGN KEY (`User_user_id`)
    REFERENCES `web_dev_db`.`User` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `web_dev_db`.`PostComment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`PostComment` (
  `comment_id` INT NOT NULL,
  `comment_content` TINYTEXT NULL,
  `comment_timestamp` TIMESTAMP(1) NULL,
  `Post_post_id` INT NOT NULL,
  `Post_User_user_id` INT NOT NULL,
  PRIMARY KEY (`comment_id`, `Post_post_id`, `Post_User_user_id`),
  INDEX `fk_PostComment_Post1_idx` (`Post_post_id` ASC, `Post_User_user_id` ASC),
  CONSTRAINT `fk_PostComment_Post1`
    FOREIGN KEY (`Post_post_id` , `Post_User_user_id`)
    REFERENCES `web_dev_db`.`Post` (`post_id` , `User_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;