package com.example.apisocialmeli.dto;

import java.util.List;

public class FollowersListResponseDTO {

    private int userId;
    private String userName;
    private List<FollowerDTO> followers;

    public FollowersListResponseDTO(int userId, String userName, List<FollowerDTO> followers) {
        this.userId = userId;
        this.userName = userName;
        this.followers = followers;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public List<FollowerDTO> getFollowers() {
        return followers;
    }
}