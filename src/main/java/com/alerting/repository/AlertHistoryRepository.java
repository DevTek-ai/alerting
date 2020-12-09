package com.alerting.repository;

import com.alerting.domain.AlertHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AlertHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {

}
