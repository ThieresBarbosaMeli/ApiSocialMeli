package com.example.apisocialmeli.dto.response;

public class FollowedDTO {

    private int userId;
    private String userName;

    public FollowedDTO(int userId, String userName) {
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