package com.example.apisocialmeli.service;

import com.example.apisocialmeli.domain.Post;
import com.example.apisocialmeli.domain.Product;

import java.time.LocalDate;
import java.util.List;

public interface PostService {

    void createPost(int id,
                    int userId,
                    LocalDate date,
                    Product product,
                    int category,
                    double price,
                    boolean hasPromo,
                    Double discount);

    List<Post> getPostsByUser(int userId);

    List<Post> getFeedForUser(int userId, String order);

    int countPromoPostsByUser(int userId);

    List<Post> getPromoPostsByUser(int userId);
}

