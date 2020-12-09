package com.alerting.repository;

import com.alerting.domain.Binary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Binary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BinaryRepository extends JpaRepository<Binary, Long> {

}
