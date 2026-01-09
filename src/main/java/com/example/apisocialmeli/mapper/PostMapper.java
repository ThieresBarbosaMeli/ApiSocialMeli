package com.example.apisocialmeli.mapper;

import com.example.apisocialmeli.domain.Post;
import com.example.apisocialmeli.dto.response.PostResponseDTO;
import com.example.apisocialmeli.dto.response.PostResponseDTO.ProductDTO;

public final class PostMapper {

    private PostMapper() {
    }

    public static PostResponseDTO toResponse(Post post) {
        ProductDTO product = new ProductDTO(
                post.getProduct().getProductId(),
                post.getProduct().getProductName(),
                post.getProduct().getType(),
                post.getProduct().getBrand(),
                post.getProduct().getColor(),
                post.getProduct().getNotes()
        );
        return new PostResponseDTO(
                post.getUserId(),
                post.getId(),
                post.getDate(),
                product,
                post.getCategory(),
                post.getPrice(),
                post.isHasPromo(),
                post.getDiscount()
        );
    }
}
