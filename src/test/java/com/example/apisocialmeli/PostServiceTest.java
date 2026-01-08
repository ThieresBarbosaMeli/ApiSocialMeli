package com.example.apisocialmeli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostRepository postRepository;
    private UserService userService;
    private PostService postService;

    @BeforeEach
    void setUp() {
        postRepository = new PostRepository();
        userService = mock(UserService.class);
        postService = new PostService(postRepository, userService);
    }

    @Test
    void createPostShouldPersistInRepository() {
        Product product = new Product(1, "Mouse", "Acessório", "Logi", "Preto", "Sem fio");

        postService.createPost(
                100,
                10,
                LocalDate.now(),
                product,
                200,
                150.0,
                false,
                null
        );

        Post saved = postRepository.findById(100);
        assertEquals(10, saved.getUserId());
        assertEquals("Mouse", saved.getProduct().getProductName());
        assertEquals(200, saved.getCategory());
        assertEquals(150.0, saved.getPrice());
        assertEquals(false, saved.isHasPromo());
    }

    @Test
    void getFeedShouldReturnEmptyWhenFollowingIsEmpty() {
        when(userService.getFollowing(1)).thenReturn(Collections.emptySet());

        assertEquals(0, postService.getFeedForUser(1, "").size());
    }

    @Test
    void getFeedShouldReturnPostsFromFollowedUsersOnly() {
        when(userService.getFollowing(1)).thenReturn(Set.of(10));
        LocalDate hoje = LocalDate.now();

        postRepository.save(new Post(1, 10, hoje, dummyProduct(), 100, 50.0, false, null));
        postRepository.save(new Post(2, 20, hoje, dummyProduct(), 100, 60.0, false, null));

        var feed = postService.getFeedForUser(1, "");
        assertEquals(1, feed.size());
        assertEquals(10, feed.get(0).getUserId());
    }

    @Test
    void getFeedShouldFilterPostsOlderThanTwoWeeks() {
        when(userService.getFollowing(1)).thenReturn(Set.of(10));

        postRepository.save(new Post(1, 10, LocalDate.now().minusDays(1), dummyProduct(), 100, 10, false, null));
        postRepository.save(new Post(2, 10, LocalDate.now().minusDays(15), dummyProduct(), 100, 10, false, null));

        var feed = postService.getFeedForUser(1, "");
        assertEquals(1, feed.size());
        assertEquals(1, feed.get(0).getId());
    }

    @Test
    void getFeedShouldRespectDateOrderAscending() {
        when(userService.getFollowing(1)).thenReturn(Set.of(10));
        postRepository.save(new Post(1, 10, LocalDate.now().minusDays(1), dummyProduct(), 100, 10, false, null));
        postRepository.save(new Post(2, 10, LocalDate.now(), dummyProduct(), 100, 10, false, null));

        var feed = postService.getFeedForUser(1, "date_asc");
        assertEquals(1, feed.get(0).getId());
        assertEquals(2, feed.get(1).getId());
    }

    @Test
    void getFeedShouldRespectDateOrderDescendingByDefault() {
        when(userService.getFollowing(1)).thenReturn(Set.of(10));
        postRepository.save(new Post(1, 10, LocalDate.now().minusDays(1), dummyProduct(), 100, 10, false, null));
        postRepository.save(new Post(2, 10, LocalDate.now(), dummyProduct(), 100, 10, false, null));

        var feed = postService.getFeedForUser(1, "");
        assertEquals(2, feed.get(0).getId());
        assertEquals(1, feed.get(1).getId());
    }

    @Test
    void countPromoPostsShouldReturnCorrectValue() {
        postRepository.save(new Post(1, 10, LocalDate.now(), dummyProduct(), 100, 10, true, 0.2));
        postRepository.save(new Post(2, 10, LocalDate.now(), dummyProduct(), 100, 10, false, null));
        postRepository.save(new Post(3, 10, LocalDate.now(), dummyProduct(), 100, 10, true, 0.1));

        int count = postService.countPromoPostsByUser(10);
        assertEquals(2, count);
    }

    @Test
    void getPromoPostsShouldReturnOnlyPromotionalPosts() {
        postRepository.save(new Post(1, 10, LocalDate.now(), dummyProduct(), 100, 10, true, 0.2));
        postRepository.save(new Post(2, 10, LocalDate.now(), dummyProduct(), 100, 10, false, null));

        var promoPosts = postService.getPromoPostsByUser(10);
        assertEquals(1, promoPosts.size());
        assertEquals(1, promoPosts.get(0).getId());
    }

    private Product dummyProduct() {
        return new Product(1, "Nome", "Tipo", "Marca", "Cor", "Notas");
    }
}