--liquibase formatted sql

--changeset mirza:release1.1.alters.sql

ALTER TABLE `alert_definition`
ADD COLUMN `condition` VARCHAR(45) NULL AFTER `query_statement`,
ADD COLUMN `type` VARCHAR(45) NULL AFTER `condition`,
ADD COLUMN `attribute` VARCHAR(45) NULL AFTER `type`,
ADD COLUMN `behaviour` VARCHAR(45) NULL AFTER `attribute`,
ADD COLUMN `from` DATETIME NULL AFTER `behaviour`,
ADD COLUMN `to` DATETIME NULL AFTER `from`;
