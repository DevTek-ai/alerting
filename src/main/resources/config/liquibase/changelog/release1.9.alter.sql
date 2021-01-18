--liquibase formatted sql

--changeset mirza:release1.9.alters.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------

ALTER TABLE `alert_definition`
ADD COLUMN `body` VARCHAR(255) NULL AFTER `recipient_phone_number`;

