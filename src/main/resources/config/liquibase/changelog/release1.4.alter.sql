--liquibase formatted sql

--changeset mirza:release1.4.alters.sql

ALTER TABLE `alert_definition`
ADD COLUMN `behaviour_selection` VARCHAR(255) NULL AFTER `attribute_selection`,
ADD COLUMN `condition_selection` VARCHAR(255) NULL AFTER `behaviour_selection`,
ADD COLUMN `custom_attribute_selection` VARCHAR(255) NULL AFTER `condition_selection`,
ADD COLUMN `from_date` DATETIME NULL AFTER `custom_attribute_selection`,
ADD COLUMN `to_date` DATETIME NULL AFTER `from_date`;

