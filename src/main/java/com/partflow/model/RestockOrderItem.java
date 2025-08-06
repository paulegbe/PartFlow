package com.partflow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "restock_order_item")
public class RestockOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int quantity;
    private double unitCost;
    
    @ManyToOne
    @JoinColumn(name = "restock_order_id")
    private RestockOrder restockOrder;
    
    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;

    // Constructors
    public RestockOrderItem() {}

    public RestockOrderItem(Part part, int quantity, double unitCost) {
        this.part = part;
        this.quantity = quantity;
        this.unitCost = unitCost;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitCost() { return unitCost; }
    public void setUnitCost(double unitCost) { this.unitCost = unitCost; }

    public RestockOrder getRestockOrder() { return restockOrder; }
    public void setRestockOrder(RestockOrder restockOrder) { this.restockOrder = restockOrder; }

    public Part getPart() { return part; }
    public void setPart(Part part) { this.part = part; }

    // Helper methods
    public double getTotalCost() {
        return quantity * unitCost;
    }
} 