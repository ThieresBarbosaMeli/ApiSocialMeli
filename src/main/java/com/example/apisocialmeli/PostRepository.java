package com.example.apisocialmeli;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Repository
public class PostRepository {

    private Map<Integer, Post> posts = new HashMap<>();

    public void save(Post post) {
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }
}