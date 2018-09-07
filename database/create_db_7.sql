-- MySQL Script generated by MySQL Workbench
-- Fri 07 Sep 2018 10:36:24 PM EEST
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
-- Table `web_dev_db`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `name` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  `phone_number` VARCHAR(45) NULL,
  `profile_picture` VARCHAR(45) NULL,
  `city` VARCHAR(45) NULL,
  `profession` VARCHAR(45) NULL,
  `company` VARCHAR(45) NULL,
  `education` VARCHAR(45) NULL,
  `public_phone_number` TINYINT(1) NULL,
  `public_city` TINYINT(1) NULL,
  `public_profession` TINYINT(1) NULL,
  `public_company` TINYINT(1) NULL,
  `public_education` TINYINT(1) NULL,
  `is_admin` TINYINT(1) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `web_dev_db`.`post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`post` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(280) NULL,
  `timestamp` DATETIME NULL,
  `is_advertisment` TINYINT(1) NULL,
  `is_public` TINYINT(1) NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`, `user_id`),
  INDEX `fk_post_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_post_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `web_dev_db`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `web_dev_db`.`post_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`post_comment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(280) NULL,
  `comment_timestamp` DATETIME NULL,
  `post_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`, `post_id`, `user_id`),
  INDEX `fk_PostComment_post1_idx` (`post_id` ASC),
  CONSTRAINT `fk_PostComment_post1`
    FOREIGN KEY (`post_id`)
    REFERENCES `web_dev_db`.`post` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `web_dev_db`.`user_network`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`user_network` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_1` INT NOT NULL,
  `user_2` INT NOT NULL,
  `is_accepted` TINYINT(1) NULL,
  PRIMARY KEY (`id`, `user_1`, `user_2`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `web_dev_db`.`post_interest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`post_interest` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `post_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`, `post_id`, `user_id`),
  INDEX `fk_post_interests_post1_idx` (`post_id` ASC),
  CONSTRAINT `fk_post_interests_post1`
    FOREIGN KEY (`post_id`)
    REFERENCES `web_dev_db`.`post` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `web_dev_db`.`chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`chat` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_1` INT NOT NULL,
  `user_2` INT NOT NULL,
  PRIMARY KEY (`id`, `user_1`, `user_2`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `web_dev_db`.`chat_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`chat_history` (
  `chat_id` INT NOT NULL,
  `sender_user_id` INT NOT NULL,
  `message_content` VARCHAR(280) NULL,
  `timestamp` DATETIME NULL,
  PRIMARY KEY (`chat_id`, `sender_user_id`),
  CONSTRAINT `fk_chat_history_chat1`
    FOREIGN KEY (`chat_id`)
    REFERENCES `web_dev_db`.`chat` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `web_dev_db`.`notifications`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `web_dev_db`.`notifications` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `notified_by_user` INT NOT NULL,
  `post_id` INT NOT NULL,
  `interest` TINYINT(1) NULL,
  `comment` TINYINT(1) NULL,
  `timestamp` VARCHAR(45) NULL,
  PRIMARY KEY (`id`, `user_id`, `notified_by_user`, `post_id`),
  INDEX `fk_notifications_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_notifications_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `web_dev_db`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
