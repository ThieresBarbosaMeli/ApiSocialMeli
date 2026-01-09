package com.example.apisocialmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PostResponseDTO {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("post_id")
    private int postId;

    private String date;
    private ProductDTO product;
    private int category;
    private double price;

    @JsonProperty("has_promo")
    private boolean hasPromo;

    private Double discount;

    public PostResponseDTO(int userId,
                           int postId,
                           LocalDate date,
                           ProductDTO product,
                           int category,
                           double price,
                           boolean hasPromo,
                           Double discount) {
        this.userId = userId;
        this.postId = postId;
        this.date = date.format(DATE_FORMATTER);
        this.product = product;
        this.category = category;
        this.price = price;
        this.hasPromo = hasPromo;
        this.discount = discount;
    }

    public int getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
    }

    public String getDate() {
        return date;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public int getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isHasPromo() {
        return hasPromo;
    }

    public Double getDiscount() {
        return discount;
    }

    public static class ProductDTO {

        @JsonProperty("product_id")
        private int productId;

        @JsonProperty("product_name")
        private String productName;

        private String type;
        private String brand;
        private String color;
        private String notes;

        public ProductDTO(int productId, String productName, String type, String brand, String color, String notes) {
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
}