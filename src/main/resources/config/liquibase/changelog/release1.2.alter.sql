--liquibase formatted sql

--changeset mirza:release1.2.alters.sql

ALTER TABLE `alert_definition`
ADD COLUMN `custom` VARCHAR(45) NULL AFTER `to`;
