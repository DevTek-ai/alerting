package com.alerting.repository;

import com.alerting.domain.UserType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {

}
