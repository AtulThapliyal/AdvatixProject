package com.employee.advatixAPI.repository.client;

import com.employee.advatixAPI.entity.address.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    City getCityByStateIdAndCountryId(Integer stateId, Integer countryId);

    City getCityByCityId(Integer cityId);
}
