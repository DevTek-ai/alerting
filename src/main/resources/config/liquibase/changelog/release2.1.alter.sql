--liquibase formatted sql

--changeset mirza:release2.1.alters.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------


ALTER TABLE `alert_history`
ADD COLUMN `user_login` VARCHAR(255) NULL AFTER `triggered_type`;

