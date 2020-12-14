--liquibase formatted sql

--changeset mirza:release1.alters.sql


ALTER TABLE `alert_definition`
ADD COLUMN `query_statement` VARCHAR(256) NULL AFTER `statement_id`;


ALTER TABLE `alert_history`
ADD COLUMN `category` INT NULL  AFTER `triggered_alert_id`,
ADD COLUMN `created_date` DATETIME NULL  AFTER `category`,
ADD COLUMN `modified_date` DATETIME NULL  AFTER `created_date`;
