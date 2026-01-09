package com.example.apisocialmeli.repository;

import com.example.apisocialmeli.domain.Post;

import java.util.List;

public interface PostRepository {

    void save(Post post);

    Post findById(int id);

    List<Post> findAll();
}