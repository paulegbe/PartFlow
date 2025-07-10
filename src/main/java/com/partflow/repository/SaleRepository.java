package com.partflow.repository;

import com.partflow.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findBySaleDate(LocalDateTime saleDate);

    List<Sale> findByPartName(String partName);

}
