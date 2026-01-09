package com.example.apisocialmeli;

import com.example.apisocialmeli.domain.Post;
import com.example.apisocialmeli.domain.Product;
import com.example.apisocialmeli.domain.User;
import com.example.apisocialmeli.dto.response.PostResponseDTO;
import com.example.apisocialmeli.mapper.PostMapper;
import com.example.apisocialmeli.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapperTest {

    @Test
    void userMapperShouldMapFollowerAndFollowed() {
        User user = new User(10, "Alice", "alice@test.com");

        var follower = UserMapper.toFollowerDTO(user);
        var followed = UserMapper.toFollowedDTO(user);

        assertEquals(10, follower.getUserId());
        assertEquals("Alice", follower.getUserName());
        assertEquals(10, followed.getUserId());
        assertEquals("Alice", followed.getUserName());
    }

    @Test
    void postMapperShouldMapPostToResponse() {
        Product product = new Product(1, "Mouse", "Periferico", "Logi", "Preto", "RGB");
        Post post = new Post(5, 10, LocalDate.of(2024, 5, 1), product, 100, 250.0, true, 0.2);

        PostResponseDTO dto = PostMapper.toResponse(post);

        assertEquals(10, dto.getUserId());
        assertEquals(5, dto.getPostId());
        assertEquals("01-05-2024", dto.getDate());
        assertEquals("Mouse", dto.getProduct().getProductName());
        assertTrue(dto.isHasPromo());
        assertEquals(0.2, dto.getDiscount());
    }
}
