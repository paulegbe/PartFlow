package com.partflow.repository;

import com.partflow.model.RestockOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RestockOrderRepository extends JpaRepository<RestockOrder, Long> {

    // Find orders by status
    List<RestockOrder> findByStatus(String status);
    
    // Find orders by vendor
    List<RestockOrder> findByVendorId(Long vendorId);
    
    // Find orders by date range
    List<RestockOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find pending orders
    List<RestockOrder> findByStatusOrderByOrderDateDesc(String status);
    
    // Find orders with expected delivery in the next X days
    @Query("SELECT ro FROM RestockOrder ro WHERE ro.expectedDelivery BETWEEN :startDate AND :endDate")
    List<RestockOrder> findOrdersWithExpectedDeliveryBetween(@Param("startDate") LocalDateTime startDate, 
                                                           @Param("endDate") LocalDateTime endDate);
    
    // Find orders by vendor and status
    List<RestockOrder> findByVendorIdAndStatus(Long vendorId, String status);
    
    // Count orders by status
    long countByStatus(String status);
    
    // Find orders with total cost greater than specified amount
    List<RestockOrder> findByTotalCostGreaterThan(double amount);
} 