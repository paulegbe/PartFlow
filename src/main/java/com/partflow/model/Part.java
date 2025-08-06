package com.partflow.model;

import jakarta.persistence.*;

@Entity
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String partName;
    private String partNumber;
    private double price;
    private int quantity;
    private boolean inStock;
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    private int restockThreshold = 10; // minimum allowed quantity
    private int restockAmount = 200;    // how many to order when below threshold

    @Transient
    private boolean selected; // not persisted, used for UI selection

    public Part() {}  // required by JPA

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }

    public String getPartName() { return partName; }
    public void setPartName(String partName) { this.partName = partName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
        this.inStock = quantity > 0; // Automatically update inStock based on quantity
    }

    public boolean isInStock() {
        return quantity > 0;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public int getRestockThreshold() {
        return restockThreshold;
    }

    public void setRestockThreshold(int restockThreshold) {
        this.restockThreshold = restockThreshold;
    }

    public int getRestockAmount() {
        return restockAmount;
    }

    public void setRestockAmount(int restockAmount) {
        this.restockAmount = restockAmount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
