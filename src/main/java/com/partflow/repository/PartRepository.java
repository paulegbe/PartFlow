package com.partflow.repository;

import com.partflow.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {
    Part findByPartNumber(String partNumber);
    Part findByPartName(String partName);
}
