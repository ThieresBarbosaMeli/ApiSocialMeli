package com.example.apisocialmeli.service.impl;

import com.example.apisocialmeli.domain.Post;
import com.example.apisocialmeli.domain.Product;
import com.example.apisocialmeli.exception.ErrorMessages;
import com.example.apisocialmeli.repository.PostRepository;
import com.example.apisocialmeli.service.PostService;
import com.example.apisocialmeli.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public void createPost(int id,
                           int userId,
                           LocalDate date,
                           Product product,
                           int category,
                           double price,
                           boolean hasPromo,
                           Double discount) {

        if (hasPromo && discount == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Discount is required for promotional posts");
        }

        userService.getUserById(userId);
        Post post = new Post(userId, date, product, category, price, hasPromo, discount);
        postRepository.save(post);
    }

    @Override
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

    @Override
    public List<Post> getFeedForUser(int userId, String order) {
        Comparator<Post> comparator = resolveDateOrder(order);

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

        result.sort(comparator);
        return result;
    }

    @Override
    public int countPromoPostsByUser(int userId) {
        return (int) postRepository.findAll().stream()
                .filter(post -> post.getUserId() == userId && post.isHasPromo())
                .count();
    }

    @Override
    public List<Post> getPromoPostsByUser(int userId) {
        return postRepository.findAll().stream()
                .filter(post -> post.getUserId() == userId && post.isHasPromo())
                .toList();
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getAllPromoPosts() {
        return postRepository.findAll().stream()
                .filter(Post::isHasPromo)
                .toList();
    }

    private Comparator<Post> resolveDateOrder(String order) {
        String normalized = order == null ? "" : order.trim().toLowerCase();

        return switch (normalized) {
            case "", "date_desc" -> Comparator.comparing(Post::getDate).reversed();
            case "date_asc" -> Comparator.comparing(Post::getDate);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    ErrorMessages.INVALID_DATE_ORDER);
        };
    }
}
