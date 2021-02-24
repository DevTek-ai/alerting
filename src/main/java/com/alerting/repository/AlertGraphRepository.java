package com.alerting.repository;

import com.alerting.domain.AlertDefinition;
import com.alerting.domain.AlertGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AlertDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlertGraphRepository extends JpaRepository<AlertGraph, Long> {

}
