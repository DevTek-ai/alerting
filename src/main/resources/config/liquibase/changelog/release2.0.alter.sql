--liquibase formatted sql

--changeset mirza:release2.0.alters.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------

ALTER TABLE `alert_definition`
ADD COLUMN `user_types` VARCHAR(255) NULL AFTER `body`;
