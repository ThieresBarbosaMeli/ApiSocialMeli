package com.example.apisocialmeli;

import java.time.LocalDate;

public class PostResponseDTO {
    private int id;
    private int userId;
    private LocalDate date;
    private String productName;
    private String productType;
    private String productBrand;
    private String productColor;
    private String notes;

    public PostResponseDTO(Post post) {
        this.id = post.getId();
        this.userId = post.getUserId();
        this.date = post.getDate();
        this.productName = post.getProductName();
        this.productType = post.getProductType();
        this.productBrand = post.getProductBrand();
        this.productColor = post.getProductColor();
        this.notes = post.getNotes();
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public LocalDate getDate() { return date; }
    public String getProductName() { return productName; }
    public String getProductType() { return productType; }
    public String getProductBrand() { return productBrand; }
    public String getProductColor() { return productColor; }
    public String getNotes() { return notes; }
}