package com.ourvoice.mgnrega.repository;

import com.ourvoice.mgnrega.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long> {
    Optional<District> findByNameIgnoreCase(String name);
}
