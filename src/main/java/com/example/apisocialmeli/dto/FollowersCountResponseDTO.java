package com.example.apisocialmeli.dto;

public class FollowersCountResponseDTO {

    private int userId;
    private String userName;
    private int followersCount;

    public FollowersCountResponseDTO(int userId, String userName, int followersCount) {
        this.userId = userId;
        this.userName = userName;
        this.followersCount = followersCount;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getFollowersCount() {
        return followersCount;
    }
}