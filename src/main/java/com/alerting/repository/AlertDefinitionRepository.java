package com.alerting.repository;

import com.alerting.domain.AlertDefinition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AlertDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlertDefinitionRepository extends JpaRepository<AlertDefinition, Long> {

}
