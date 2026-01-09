package com.example.apisocialmeli.repository;

import com.example.apisocialmeli.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPostRepository implements PostRepository {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    @Override
    public void save(Post post) {
        posts.put(post.getId(), post);
    }

    @Override
    public Post findById(int id) {
        return posts.get(id);
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }
}

