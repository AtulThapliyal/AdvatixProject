package com.employee.advatixAPI.repository.lpn;

import com.employee.advatixAPI.entity.manifest.ManifestMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManifestRepository extends MongoRepository<ManifestMapping, Integer> {
    Optional<List<ManifestMapping>> findAllByManifestNumber(String number);
}
