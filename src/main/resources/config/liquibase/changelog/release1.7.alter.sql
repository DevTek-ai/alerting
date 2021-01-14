--liquibase formatted sql

--changeset mirza:release1.7.alters.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------

ALTER TABLE `alert_history`
ADD COLUMN `triggered_id` BIGINT(20) NULL AFTER `modified_date`,
ADD COLUMN `triggered_type` VARCHAR(255) NULL AFTER `triggered_id`;
