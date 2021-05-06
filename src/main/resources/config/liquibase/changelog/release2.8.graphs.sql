--liquibase formatted sql

--changeset mirza:release2.8.graphs.sql

-- -----------------------------------------------------
-- Table `alerting`.`alert_definition`
-- -----------------------------------------------------


DROP VIEW IF EXISTS `view_alerting_graph`;
CREATE VIEW `view_alerting_graph` AS
select id, user_login, count(category) as count,Month(created_date) as month_value,
case when Month(created_date) = month(CURDATE()) then 'month3' when Month(created_date) =  MONTH(DATE_SUB(NOW(),INTERVAL 1 MONTH))  then 'month2'
 when Month(created_date) =  MONTH(DATE_SUB(NOW(),INTERVAL 2 MONTH))  then 'month3'
 when Month(created_date) =  MONTH(Date_add(NOW(),INTERVAL 1 MONTH)) then 'month4'
end as months,
case when category=2 then 'info'
when category = 3  then 'error'
when category = 1 then 'warning'
else 'default' end as category
from alert_history
where created_date is not  null
group by category,month_value,user_login
having month_value in (month(CURDATE()), MONTH(DATE_SUB(NOW(),INTERVAL 1 MONTH)),
MONTH(DATE_SUB(NOW(),INTERVAL 2 MONTH)),MONTH(Date_add(NOW(),INTERVAL 1 MONTH)) );