package com.partflow.repository;

import com.partflow.model.RestockOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestockOrderItemRepository extends JpaRepository<RestockOrderItem, Long> {

    // Find items by restock order
    List<RestockOrderItem> findByRestockOrderId(Long restockOrderId);
    
    // Find items by part
    List<RestockOrderItem> findByPartId(Long partId);
    
    // Find items by restock order and part
    List<RestockOrderItem> findByRestockOrderIdAndPartId(Long restockOrderId, Long partId);
} 