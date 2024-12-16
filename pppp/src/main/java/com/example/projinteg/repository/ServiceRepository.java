package com.example.projinteg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projinteg.entity.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    // Additional queries if needed, e.g.:
    // Optional<ServiceEntity> findByServiceName(String serviceName);
}
