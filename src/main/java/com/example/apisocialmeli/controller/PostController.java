package com.example.apisocialmeli.controller;

import com.example.apisocialmeli.domain.Product;
import com.example.apisocialmeli.domain.User;
import com.example.apisocialmeli.dto.request.CreatePostRequest;
import com.example.apisocialmeli.dto.request.CreatePromoPostRequest;
import com.example.apisocialmeli.service.PostService;
import com.example.apisocialmeli.service.UserService;
import com.example.apisocialmeli.dto.response.FollowedPostsResponseDTO;
import com.example.apisocialmeli.dto.response.PostResponseDTO;
import com.example.apisocialmeli.dto.response.PromoCountResponseDTO;
import com.example.apisocialmeli.dto.response.PromoPostListResponseDTO;
import com.example.apisocialmeli.mapper.PostMapper;
import com.example.apisocialmeli.exception.ErrorMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Validated
@RestController
@RequestMapping("/products")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String INVALID_DATE_ORDER_MESSAGE = ErrorMessages.INVALID_DATE_ORDER;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public List<PostResponseDTO> getAllPosts(
            @RequestParam(value = "user_id", required = false) Integer userId) {
        if (userId != null) {
            userService.getUserById(userId);
            return postService.getPostsByUser(userId).stream()
                    .map(PostMapper::toResponse)
                    .toList();
        } else {
            return postService.getAllPosts().stream()
                    .map(PostMapper::toResponse)
                    .toList();
        }
    }

    @PostMapping("/publish")
    public void createPost(@Valid @RequestBody CreatePostRequest request) {
        registerPost(request);
    }

    @PostMapping("/promo-pub")
    public void createPromoPost(@Valid @RequestBody CreatePromoPostRequest request) {
        registerPromoPost(request);
    }

    @GetMapping("/promo-pub/count")
    public PromoCountResponseDTO getPromoCount(
            @RequestParam(value = "user_id", required = false) Integer userId) {
        if (userId != null) {
            User user = userService.getUserById(userId);
            int count = postService.countPromoPostsByUser(userId);
            return new PromoCountResponseDTO(user.getId(), user.getName(), count);
        } else {
            int count = postService.getAllPromoPosts().size();
            return new PromoCountResponseDTO(0, "Todos", count);
        }
    }

    @GetMapping("/promo-pub/list")
    public Object getPromoPosts(
            @RequestParam(value = "user_id", required = false) Integer userId) {
        if (userId != null) {
            User user = userService.getUserById(userId);
            List<PostResponseDTO> posts = postService.getPromoPostsByUser(userId).stream()
                    .map(PostMapper::toResponse)
                    .toList();
            return new PromoPostListResponseDTO(user.getId(), user.getName(), posts);
        } else {
            return postService.getAllPromoPosts().stream()
                    .map(PostMapper::toResponse)
                    .toList();
        }
    }

    @GetMapping("/followed/{userId}/list")
    public FollowedPostsResponseDTO getFollowedPosts(
            @PathVariable @Positive(message = ErrorMessages.USER_ID_POSITIVE_PATH) int userId,
            @RequestParam(required = false, defaultValue = "") String order) {
        validateDateOrder(order);
        userService.getUserById(userId);

        List<PostResponseDTO> posts = postService.getFeedForUser(userId, order).stream()
                .map(PostMapper::toResponse)
                .toList();

        return new FollowedPostsResponseDTO(userId, posts);
    }

    private void registerPost(CreatePostRequest request) {
        userService.getUserById(request.getUserId());

        LocalDate date = LocalDate.parse(request.getDate(), DATE_FORMATTER);

        Product product = new Product(
                request.getProduct().getProductId(),
                request.getProduct().getProductName(),
                request.getProduct().getType(),
                request.getProduct().getBrand(),
                request.getProduct().getColor(),
                request.getProduct().getNotes()
        );

        postService.createPost(
                request.getPostId() != null ? request.getPostId() : 0,
                request.getUserId(),
                date,
                product,
                request.getCategory(),
                request.getPrice(),
                request.getHasPromo(),
                request.getDiscount()
        );
    }

    private void registerPromoPost(CreatePromoPostRequest request) {
        userService.getUserById(request.getUserId());

        LocalDate date = LocalDate.parse(request.getDate(), DATE_FORMATTER);

        Product product = new Product(
                request.getProduct().getProductId(),
                request.getProduct().getProductName(),
                request.getProduct().getType(),
                request.getProduct().getBrand(),
                request.getProduct().getColor(),
                request.getProduct().getNotes()
        );

        postService.createPost(
                request.getPostId() != null ? request.getPostId() : 0,
                request.getUserId(),
                date,
                product,
                request.getCategory(),
                request.getPrice(),
                request.getHasPromo(),
                request.getDiscount()
        );
    }

    private void validateDateOrder(String order) {
        if (order.isEmpty()) return;

        if (!order.equalsIgnoreCase("date_asc") && !order.equalsIgnoreCase("date_desc")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_DATE_ORDER_MESSAGE);
        }
    }
}
