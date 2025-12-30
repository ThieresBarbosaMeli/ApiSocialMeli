package com.example.apisocialmeli;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    public static class CreatePostRequest {
        private int id;
        private int userId;
        private String date; // formato ISO: yyyy-MM-dd
        private String productName;
        private String productType;
        private String productBrand;
        private String productColor;
        private String notes;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getProductType() { return productType; }
        public void setProductType(String productType) { this.productType = productType; }

        public String getProductBrand() { return productBrand; }
        public void setProductBrand(String productBrand) { this.productBrand = productBrand; }

        public String getProductColor() { return productColor; }
        public void setProductColor(String productColor) { this.productColor = productColor; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    @PostMapping
    public void createPost(@RequestBody CreatePostRequest request) {
        LocalDate date = LocalDate.parse(request.getDate());
        postService.createPost(
                request.getId(),
                request.getUserId(),
                date,
                request.getProductName(),
                request.getProductType(),
                request.getProductBrand(),
                request.getProductColor(),
                request.getNotes()
        );
    }

    @GetMapping("/followed/{userId}")
    public List<PostResponseDTO> getFeed(@PathVariable int userId) {
        return postService.getFeedForUser(userId).stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }
}