--liquibase formatted sql

--changeset mirza:release2.2.alters.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------


ALTER TABLE `alert_history`
ADD COLUMN `attribute` VARCHAR(255) NULL AFTER `user_login`,
ADD COLUMN `behaviour` VARCHAR(255) NULL AFTER `attribute`;

