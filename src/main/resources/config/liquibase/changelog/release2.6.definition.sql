--liquibase formatted sql

--changeset mirza:release2.6.definition.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------


DELETE FROM `alert_definition` WHERE (`id` = '14');
DELETE FROM `alert_definition` WHERE (`id` = '13');