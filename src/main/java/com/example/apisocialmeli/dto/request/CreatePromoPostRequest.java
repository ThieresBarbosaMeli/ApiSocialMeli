package com.example.apisocialmeli.dto.request;

import com.example.apisocialmeli.exception.ErrorMessages;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({
        "user_id", "date", "product", "category", "price", "post_id", "has_promo", "discount"
})
public class CreatePromoPostRequest {

    @JsonProperty("user_id")
    @Positive(message = ErrorMessages.USER_ID_POSITIVE)
    private int userId;

    @NotBlank(message = ErrorMessages.DATE_NOT_BLANK)
    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = ErrorMessages.DATE_PATTERN)
    private String date;

    @Valid
    @NotNull(message = ErrorMessages.PRODUCT_NOT_NULL)
    private ProductRequest product;

    @Positive(message = ErrorMessages.CATEGORY_POSITIVE)
    private int category;

    @DecimalMin(value = "0.0", inclusive = false, message = ErrorMessages.PRICE_MIN)
    @DecimalMax(value = "10000000.0", message = ErrorMessages.PRICE_MAX)
    private double price;

    @JsonProperty("post_id")
    private Integer postId = 0;

    @JsonProperty("has_promo")
    @NotNull(message = ErrorMessages.HAS_PROMO_NOT_NULL)
    private Boolean hasPromo;

    @DecimalMin(value = "0.0", inclusive = false, message = ErrorMessages.DISCOUNT_MIN)
    @DecimalMax(value = "1.0", message = ErrorMessages.DISCOUNT_MAX)
    private Double discount;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ProductRequest getProduct() {
        return product;
    }

    public void setProduct(ProductRequest product) {
        this.product = product;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Boolean getHasPromo() {
        return hasPromo;
    }

    public void setHasPromo(Boolean hasPromo) {
        this.hasPromo = hasPromo;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
