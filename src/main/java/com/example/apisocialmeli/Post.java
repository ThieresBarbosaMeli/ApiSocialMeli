package com.example.apisocialmeli;

import java.time.LocalDate;

public class Post {
    private int id;
    private int userId;
    private LocalDate date;
    private String productName;
    private String productType;
    private String productBrand;
    private String productColor;
    private String notes;

    public Post(int id,
                int userId,
                LocalDate date,
                String productName,
                String productType,
                String productBrand,
                String productColor,
                String notes) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.productName = productName;
        this.productType = productType;
        this.productBrand = productBrand;
        this.productColor = productColor;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public String getProductColor() {
        return productColor;
    }

    public String getNotes() {
        return notes;
    }
}