package com.example.apisocialmeli;

import com.example.apisocialmeli.dto.PostResponseDTO;
import com.example.apisocialmeli.dto.PromoCountResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    public static class CreatePostRequest {

        @Positive(message = "post_id deve ser maior que zero")
        private int id;

        @Positive(message = "user_id deve ser maior que zero")
        private int userId;

        @NotBlank(message = "A data não pode estar vazia")
        @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "A data deve estar no formato dd-MM-aaaa")
        private String date;

        @Valid
        @NotNull(message = "O produto não pode ser nulo")
        private ProductRequest product;

        @Positive(message = "category deve ser maior que zero")
        private int category;

        @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
        @DecimalMax(value = "10000000.0", message = "O preço não pode exceder 10.000.000")
        private double price;

        @NotNull(message = "has_promo não pode ser nulo")
        private Boolean hasPromo;

        @DecimalMin(value = "0.0", inclusive = false, message = "O desconto deve ser maior que zero")
        @DecimalMax(value = "1.0", message = "O desconto não pode ser maior que 1.0")
        private Double discount;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public ProductRequest getProduct() { return product; }
        public void setProduct(ProductRequest product) { this.product = product; }

        public int getCategory() { return category; }
        public void setCategory(int category) { this.category = category; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public Boolean getHasPromo() { return hasPromo; }
        public void setHasPromo(Boolean hasPromo) { this.hasPromo = hasPromo; }

        public Double getDiscount() { return discount; }
        public void setDiscount(Double discount) { this.discount = discount; }
    }

    public static class ProductRequest {

        @Positive(message = "product_id deve ser maior que zero")
        private int productId;

        @NotBlank(message = "product_name não pode estar vazio")
        @Size(max = 40, message = "product_name não pode exceder 40 caracteres")
        @Pattern(regexp = "[\\p{L}\\d ]+", message = "product_name não pode conter caracteres especiais")
        private String productName;

        @NotBlank(message = "type não pode estar vazio")
        @Size(max = 15, message = "type não pode exceder 15 caracteres")
        @Pattern(regexp = "[\\p{L}\\d ]+", message = "type não pode conter caracteres especiais")
        private String type;

        @NotBlank(message = "brand não pode estar vazio")
        @Size(max = 25, message = "brand não pode exceder 25 caracteres")
        @Pattern(regexp = "[\\p{L}\\d ]+", message = "brand não pode conter caracteres especiais")
        private String brand;

        @NotBlank(message = "color não pode estar vazio")
        @Size(max = 15, message = "color não pode exceder 15 caracteres")
        @Pattern(regexp = "[\\p{L}\\d ]+", message = "color não pode conter caracteres especiais")
        private String color;

        @Size(max = 80, message = "notes não pode exceder 80 caracteres")
        @Pattern(regexp = "[\\p{L}\\d ]*", message = "notes não pode conter caracteres especiais")
        private String notes;

        public int getProductId() { return productId; }
        public void setProductId(int productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }

        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    @PostMapping("/publish")
    public void createPost(@Valid @RequestBody CreatePostRequest request) {
        boolean hasPromo = Boolean.TRUE.equals(request.getHasPromo());
        Double discount = hasPromo ? request.getDiscount() : null;

        if (!hasPromo && request.getDiscount() != null) {
            throw new IllegalArgumentException("Desconto só pode ser informado quando has_promo = true.");
        }

        registerPost(request, hasPromo, discount);
    }

    @PostMapping("/promo-pub")
    public void createPromoPost(@Valid @RequestBody CreatePostRequest request) {
        if (request.getDiscount() == null) {
            throw new IllegalArgumentException("Produtos em promoção devem informar o campo discount.");
        }

        registerPost(request, true, request.getDiscount());
    }

    @GetMapping("/promo-pub/count")
    public PromoCountResponseDTO getPromoCount(@RequestParam("user_id") int userId) {
        User user = userService.getUserById(userId);
        int count = postService.countPromoPostsByUser(userId);
        return new PromoCountResponseDTO(user.getId(), user.getName(), count);
    }

    private void registerPost(CreatePostRequest request, boolean hasPromo, Double discount) {
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
                request.getId(),
                request.getUserId(),
                date,
                product,
                request.getCategory(),
                request.getPrice(),
                hasPromo,
                discount
        );
    }

    @GetMapping("/followed/{userId}/list")
    public List<PostResponseDTO> getFeed(@PathVariable int userId,
                                         @RequestParam(required = false, defaultValue = "") String order) {
        validateDateOrder(order);

        return postService.getFeedForUser(userId, order).stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    private void validateDateOrder(String order) {
        if (order.isEmpty()) return;

        if (!order.equalsIgnoreCase("date_asc") && !order.equalsIgnoreCase("date_desc")) {
            throw new IllegalArgumentException("Parâmetro 'order' inválido. Use date_asc ou date_desc.");
        }
    }
}