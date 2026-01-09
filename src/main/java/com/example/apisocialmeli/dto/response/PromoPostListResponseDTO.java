package com.example.apisocialmeli.dto.response;

import java.util.List;

public class PromoPostListResponseDTO {

    private int userId;
    private String userName;
    private List<PostResponseDTO> posts;

    public PromoPostListResponseDTO(int userId, String userName, List<PostResponseDTO> posts) {
        this.userId = userId;
        this.userName = userName;
        this.posts = posts;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public List<PostResponseDTO> getPosts() {
        return posts;
    }
}