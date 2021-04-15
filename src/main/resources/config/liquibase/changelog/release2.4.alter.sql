--liquibase formatted sql

--changeset mirza:release2.4.alters.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------

ALTER TABLE `alert_history`
ADD COLUMN `status` VARCHAR(255) NULL AFTER `subject`;
