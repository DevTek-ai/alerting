--liquibase formatted sql

--changeset mirza:release2.6.definition.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------


delete from `alert_history`;