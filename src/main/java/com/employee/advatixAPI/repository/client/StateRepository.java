package com.employee.advatixAPI.repository.client;

import com.employee.advatixAPI.entity.address.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<States, Integer> {

    States getStatesByStateId(Integer stateId);

    States getStatesByStateIdAndCountryId(Integer stateId, Integer countryId);
}
