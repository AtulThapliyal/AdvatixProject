package com.employee.advatixAPI.repository.lpn;

import com.employee.advatixAPI.entity.lpn.LpnInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LpnInfoRepository extends MongoRepository<LpnInfo, Long>{
    Optional<LpnInfo> findByLpnNumber(String lpnNumber);
}
