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
    private String Vendor;



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
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        this.Vendor = vendor;
    }

}
