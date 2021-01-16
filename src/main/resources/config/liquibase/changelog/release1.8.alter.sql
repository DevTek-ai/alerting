--liquibase formatted sql

--changeset mirza:release1.8.alters.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------

ALTER TABLE `alert_definition`
ADD COLUMN `recipient_email_address` VARCHAR(255) NULL AFTER `to_date`,
ADD COLUMN `recipient_phone_number` VARCHAR(255) NULL AFTER `recipient_email_address`;

