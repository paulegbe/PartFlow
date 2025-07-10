package com.partflow.repository;

import com.partflow.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Vendor findByName(String name);
    Vendor findByContactInfo(String contactInfo);
}
