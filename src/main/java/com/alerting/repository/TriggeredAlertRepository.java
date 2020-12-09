package com.alerting.repository;

import com.alerting.domain.TriggeredAlert;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TriggeredAlert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TriggeredAlertRepository extends JpaRepository<TriggeredAlert, Long> {

}
