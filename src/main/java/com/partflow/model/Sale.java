package com.partflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private LocalDateTime saleDate;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }

    public Part getPart() { return part; }
    public void setPart(Part part) { this.part = part; }
}
