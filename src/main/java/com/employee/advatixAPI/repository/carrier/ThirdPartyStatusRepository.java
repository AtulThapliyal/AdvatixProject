package com.employee.advatixAPI.repository.carrier;

import com.employee.advatixAPI.entity.carrier.ThirdPartyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ThirdPartyStatusRepository extends JpaRepository<ThirdPartyStatus, Long> {
}
