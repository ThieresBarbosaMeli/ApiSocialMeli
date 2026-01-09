package com.example.apisocialmeli.dto.response;

public class PromoCountResponseDTO {

    private int userId;
    private String userName;
    private int promoProductsCount;

    public PromoCountResponseDTO(int userId, String userName, int promoProductsCount) {
        this.userId = userId;
        this.userName = userName;
        this.promoProductsCount = promoProductsCount;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getPromoProductsCount() {
        return promoProductsCount;
    }
}