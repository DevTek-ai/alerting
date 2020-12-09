package com.alerting.repository;

import com.alerting.domain.Schedular;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Schedular entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchedularRepository extends JpaRepository<Schedular, Long> {

}
