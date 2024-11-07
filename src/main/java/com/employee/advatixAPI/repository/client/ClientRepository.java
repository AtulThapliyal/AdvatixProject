package com.employee.advatixAPI.repository.client;

import com.employee.advatixAPI.entity.client.ClientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientInfo, Integer> {
    Optional<ClientInfo> getClientByClientId(Integer clientId);

    List<ClientInfo> findAllByEmployeeId(Integer id);
}
