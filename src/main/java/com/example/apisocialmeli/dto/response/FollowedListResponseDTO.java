package com.example.apisocialmeli.dto.response;

import java.util.List;

public class FollowedListResponseDTO {

    private int userId;
    private String userName;
    private List<FollowedDTO> followed;

    public FollowedListResponseDTO(int userId, String userName, List<FollowedDTO> followed) {
        this.userId = userId;
        this.userName = userName;
        this.followed = followed;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public List<FollowedDTO> getFollowed() {
        return followed;
    }
}