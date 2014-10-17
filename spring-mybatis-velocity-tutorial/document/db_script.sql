CREATE SCHEMA IF NOT EXISTS `test_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

CREATE TABLE IF NOT EXISTS `test_db`.`user` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `account` VARCHAR(16) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `gmt_created` DATETIME NULL,
  `gmt_modified` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
