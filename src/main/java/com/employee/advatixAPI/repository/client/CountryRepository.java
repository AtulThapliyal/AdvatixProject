package com.employee.advatixAPI.repository.client;

import com.employee.advatixAPI.entity.address.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Country getCountryByCountryId(Integer countryId);
}
