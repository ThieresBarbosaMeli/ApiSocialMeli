package com.example.apisocialmeli.dto.response;

public class FollowerDTO {

    private int userId;
    private String userName;

    public FollowerDTO(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}