package com.alerting.repository;

import com.alerting.domain.Unary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Unary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnaryRepository extends JpaRepository<Unary, Long> {

}
