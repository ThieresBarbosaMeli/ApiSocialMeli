package com.example.apisocialmeli;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public void createPost(int id,
                           int userId,
                           LocalDate date,
                           String productName,
                           String productType,
                           String productBrand,
                           String productColor,
                           String notes) {
        Post post = new Post(id, userId, date, productName, productType, productBrand, productColor, notes);
        postRepository.save(post);
    }

    public List<Post> getPostsByUser(int userId) {
        List<Post> allPosts = postRepository.findAll();
        List<Post> result = new ArrayList<>();

        for (Post post : allPosts) {
            if (post.getUserId() == userId) {
                result.add(post);
            }
        }

        return result;
    }

    public List<Post> getFeedForUser(int userId) {
        Set<Integer> following = userService.getFollowing(userId);

        if (following.isEmpty()) {
            return Collections.emptyList();
        }

        List<Post> allPosts = postRepository.findAll();
        List<Post> result = new ArrayList<>();

        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);

        for (Post post : allPosts) {
            if (following.contains(post.getUserId())) {
                // inclui posts com data >= twoWeeksAgo
                if (!post.getDate().isBefore(twoWeeksAgo)) {
                    result.add(post);
                }
            }
        }

        result.sort(Comparator.comparing(Post::getDate).reversed());

        return result;
    }
}