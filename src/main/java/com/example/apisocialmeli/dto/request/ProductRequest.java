package com.example.apisocialmeli.dto.request;

import com.example.apisocialmeli.exception.ErrorMessages;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({
        "product_id", "product_name", "type", "brand", "color", "notes"
})
public class ProductRequest {

    @JsonProperty("product_id")
    @Positive(message = ErrorMessages.PRODUCT_ID_POSITIVE)
    private int productId;

    @JsonProperty("product_name")
    @NotBlank(message = ErrorMessages.PRODUCT_NAME_NOT_BLANK)
    @Size(max = 40, message = ErrorMessages.PRODUCT_NAME_SIZE)
    @Pattern(regexp = "[\\p{L}\\d ]+", message = ErrorMessages.PRODUCT_NAME_PATTERN)
    private String productName;

    @NotBlank(message = ErrorMessages.TYPE_NOT_BLANK)
    @Size(max = 15, message = ErrorMessages.TYPE_SIZE)
    @Pattern(regexp = "[\\p{L}\\d ]+", message = ErrorMessages.TYPE_PATTERN)
    private String type;

    @NotBlank(message = ErrorMessages.BRAND_NOT_BLANK)
    @Size(max = 25, message = ErrorMessages.BRAND_SIZE)
    @Pattern(regexp = "[\\p{L}\\d ]+", message = ErrorMessages.BRAND_PATTERN)
    private String brand;

    @NotBlank(message = ErrorMessages.COLOR_NOT_BLANK)
    @Size(max = 15, message = ErrorMessages.COLOR_SIZE)
    @Pattern(regexp = "[\\p{L}\\d ]+", message = ErrorMessages.COLOR_PATTERN)
    private String color;

    @Size(max = 80, message = ErrorMessages.NOTES_SIZE)
    @Pattern(regexp = "[\\p{L}\\d ]*", message = ErrorMessages.NOTES_PATTERN)
    private String notes;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
