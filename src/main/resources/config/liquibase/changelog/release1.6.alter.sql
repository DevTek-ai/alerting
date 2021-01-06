--liquibase formatted sql

--changeset mirza:release1.5.alters.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `alert_definition` ;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE IF NOT EXISTS `alert_definition` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `message` VARCHAR(255) NULL DEFAULT NULL,
  `trigger_type` VARCHAR(255) NULL DEFAULT NULL,
  `notify` BIT(1) NULL DEFAULT NULL,
  `category` VARCHAR(255) NULL DEFAULT NULL,
  `date_created` DATETIME NULL DEFAULT NULL,
  `date_updated` DATETIME NULL DEFAULT NULL,
  `schedular_id` BIGINT(20) NULL DEFAULT NULL,
  `statement_id` BIGINT(20) NULL DEFAULT NULL,
  `query_statement` VARCHAR(256) NULL DEFAULT NULL,
  `type_selection` VARCHAR(255) NULL DEFAULT NULL,
  `attribute_selection` VARCHAR(255) NULL DEFAULT NULL,
  `behaviour_selection` VARCHAR(255) NULL DEFAULT NULL,
  `condition_selection` VARCHAR(255) NULL DEFAULT NULL,
  `custom_attribute_selection` VARCHAR(255) NULL DEFAULT NULL,
  `from_date` DATETIME NULL DEFAULT NULL,
  `to_date` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_alert_definition_schedular_id`
    FOREIGN KEY (`schedular_id`)
    REFERENCES `schedular` (`id`),
  CONSTRAINT `fk_alert_definition_statement_id`
    FOREIGN KEY (`statement_id`)
    REFERENCES `statement` (`id`))

