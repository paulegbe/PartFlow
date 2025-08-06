package com.partflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restock_order")
public class RestockOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime orderDate;
    private LocalDateTime expectedDelivery;
    private String status; // PENDING, ORDERED, DELIVERED, CANCELLED
    private double totalCost;
    private String notes;
    
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    
    @OneToMany(mappedBy = "restockOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RestockOrderItem> items = new ArrayList<>();

    // Constructors
    public RestockOrder() {
        this.orderDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public LocalDateTime getExpectedDelivery() { return expectedDelivery; }
    public void setExpectedDelivery(LocalDateTime expectedDelivery) { this.expectedDelivery = expectedDelivery; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public List<RestockOrderItem> getItems() { return items; }
    public void setItems(List<RestockOrderItem> items) { this.items = items; }

    // Helper methods
    public void addItem(RestockOrderItem item) {
        items.add(item);
        item.setRestockOrder(this);
        calculateTotalCost();
    }

    public void removeItem(RestockOrderItem item) {
        items.remove(item);
        item.setRestockOrder(null);
        calculateTotalCost();
    }

    public void calculateTotalCost() {
        this.totalCost = items.stream()
            .mapToDouble(item -> item.getQuantity() * item.getUnitCost())
            .sum();
    }

    public boolean isPending() { return "PENDING".equals(status); }
    public boolean isOrdered() { return "ORDERED".equals(status); }
    public boolean isDelivered() { return "DELIVERED".equals(status); }
    public boolean isCancelled() { return "CANCELLED".equals(status); }
} 