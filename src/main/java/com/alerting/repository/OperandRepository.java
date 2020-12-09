package com.alerting.repository;

import com.alerting.domain.Operand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Operand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperandRepository extends JpaRepository<Operand, Long> {

}
