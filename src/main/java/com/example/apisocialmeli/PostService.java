package com.example.apisocialmeli;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
                           Product product,
                           int category,
                           double price,
                           boolean hasPromo,
                           Double discount) {

        Post post = new Post(id, userId, date, product, category, price, hasPromo, discount);
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

    public List<Post> getFeedForUser(int userId, String order) {
        Set<Integer> following = userService.getFollowing(userId);

        if (following.isEmpty()) {
            return Collections.emptyList();
        }

        List<Post> allPosts = postRepository.findAll();
        List<Post> result = new ArrayList<>();

        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);

        for (Post post : allPosts) {
            if (following.contains(post.getUserId()) && !post.getDate().isBefore(twoWeeksAgo)) {
                result.add(post);
            }
        }

        Comparator<Post> comparator = Comparator.comparing(Post::getDate);
        if (!order.equalsIgnoreCase("date_asc")) {
            comparator = comparator.reversed(); // padrão: decrescente
        }

        result.sort(comparator);
        return result;
    }

    public int countPromoPostsByUser(int userId) {
        return (int) postRepository.findAll().stream()
                .filter(post -> post.getUserId() == userId && post.isHasPromo())
                .count();
    }

    public List<Post> getPromoPostsByUser(int userId) {
        return postRepository.findAll().stream()
                .filter(post -> post.getUserId() == userId && post.isHasPromo())
                .toList();
    }
}