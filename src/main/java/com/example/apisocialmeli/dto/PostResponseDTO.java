package com.example.apisocialmeli.dto;

import com.example.apisocialmeli.Post;
import com.example.apisocialmeli.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public PostResponseDTO(Post post) {
        this.userId = post.getUserId();
        this.postId = post.getId();
        this.date = post.getDate().format(DATE_FORMATTER);
        this.product = new ProductDTO(post.getProduct());
        this.category = post.getCategory();
        this.price = post.getPrice();
        this.hasPromo = post.isHasPromo();
        this.discount = post.getDiscount();
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

        public ProductDTO(Product product) {
            this.productId = product.getProductId();
            this.productName = product.getProductName();
            this.type = product.getType();
            this.brand = product.getBrand();
            this.color = product.getColor();
            this.notes = product.getNotes();
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