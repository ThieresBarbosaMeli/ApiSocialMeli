package com.example.apisocialmeli.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Product {

    @Column(name = "product_id")
    private int productId;

    @Column(name = "product_name", length = 40, nullable = false)
    private String productName;

    @Column(length = 15, nullable = false)
    private String type;

    @Column(length = 25, nullable = false)
    private String brand;

    @Column(length = 15, nullable = false)
    private String color;

    @Column(length = 80)
    private String notes;

    protected Product() {
    }

    public Product(int productId, String productName, String type, String brand, String color, String notes) {
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.brand = brand;
        this.color = color;
        this.notes = notes;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getNotes() {
        return notes;
    }
}