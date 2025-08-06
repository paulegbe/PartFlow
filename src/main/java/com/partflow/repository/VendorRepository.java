package com.partflow.repository;

import com.partflow.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Vendor findByName(String name);
    Vendor findByContactPerson(String contactPerson);
    List<Vendor> findByNameContainingIgnoreCase(String name);

}
