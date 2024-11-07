package com.employee.advatixAPI.repository.carrier;

import com.employee.advatixAPI.entity.carrier.ShipmentJourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentJourneyRepository extends JpaRepository<ShipmentJourney, Long> {
    Optional<ShipmentJourney> findByStatusAndTime(String eventDescription, String ts);
}
