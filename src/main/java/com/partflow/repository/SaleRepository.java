package com.partflow.repository;

import com.partflow.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // Existing method
    List<Sale> findByPart_PartName(String partName);

    // New: Fetch all sales within a date range (for today’s sales)
    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

    // New: Fetch all sales after a given time (for recent sales reports)
    List<Sale> findBySaleDateAfter(LocalDateTime since);
}
