package com.example.apisocialmeli;

import com.example.apisocialmeli.dto.request.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@org.springframework.test.context.ActiveProfiles("test")
class NewEndpointsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @BeforeEach
    void setUp() throws Exception {
        CreateUserRequest user1 = new CreateUserRequest();
        user1.setId(1);
        user1.setName("Alice");
        user1.setEmail("alice@test.com");
        user1.setPassword("123456");

        CreateUserRequest user2 = new CreateUserRequest();
        user2.setId(2);
        user2.setName("Bob");
        user2.setEmail("bob@test.com");
        user2.setPassword("123456");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user2)));
    }

    @Test
    void shouldListAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists());
    }

    @Test
    void shouldListAllPosts() throws Exception {
        String postPayload = """
                {
                  "user_id": 1,
                  "date": "%s",
                  "product": {
                    "product_id": 1,
                    "product_name": "Mouse",
                    "type": "Tech",
                    "brand": "Logi",
                    "color": "Black",
                    "notes": "Wireless"
                  },
                  "category": 100,
                  "price": 50.0,
                  "has_promo": false
                }
                """.formatted(LocalDate.now().format(FORMATTER));

        mockMvc.perform(post("/products/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postPayload));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].product.product_name").value("Mouse"));
    }

    @Test
    void shouldListPostsBySpecificUser() throws Exception {
        String postUser1 = """
                {
                  "user_id": 1,
                  "date": "%s",
                  "product": {
                    "product_id": 1,
                    "product_name": "Mouse",
                    "type": "Tech",
                    "brand": "Logi",
                    "color": "Black",
                    "notes": "Wireless"
                  },
                  "category": 100,
                  "price": 50.0,
                  "has_promo": false
                }
                """.formatted(LocalDate.now().format(FORMATTER));

        String postUser2 = """
                {
                  "user_id": 2,
                  "date": "%s",
                  "product": {
                    "product_id": 2,
                    "product_name": "Keyboard",
                    "type": "Tech",
                    "brand": "Razer",
                    "color": "Green",
                    "notes": "RGB"
                  },
                  "category": 100,
                  "price": 100.0,
                  "has_promo": false
                }
                """.formatted(LocalDate.now().format(FORMATTER));

        mockMvc.perform(post("/products/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postUser1));

        mockMvc.perform(post("/products/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postUser2));

        mockMvc.perform(get("/products?user_id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].product.product_name").value("Mouse"));

        mockMvc.perform(get("/products?user_id=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].product.product_name").value("Keyboard"));
    }

    @Test
    void shouldListAllPromoPostsWithoutUserId() throws Exception {
        String promoPayload = """
                {
                  "user_id": 1,
                  "date": "%s",
                  "product": {
                    "product_id": 1,
                    "product_name": "Keyboard",
                    "type": "Tech",
                    "brand": "Logi",
                    "color": "Black",
                    "notes": "RGB"
                  },
                  "category": 100,
                  "price": 100.0,
                  "has_promo": true,
                  "discount": 0.15
                }
                """.formatted(LocalDate.now().format(FORMATTER));

        mockMvc.perform(post("/products/promo-pub")
                .contentType(MediaType.APPLICATION_JSON)
                .content(promoPayload));

        mockMvc.perform(get("/products/promo-pub/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].has_promo").value(true));
    }

    @Test
    void shouldCountAllPromoPostsWithoutUserId() throws Exception {
        String promoPayload1 = """
                {
                  "user_id": 1,
                  "date": "%s",
                  "product": {
                    "product_id": 1,
                    "product_name": "Keyboard",
                    "type": "Tech",
                    "brand": "Logi",
                    "color": "Black",
                    "notes": "RGB"
                  },
                  "category": 100,
                  "price": 100.0,
                  "has_promo": true,
                  "discount": 0.15
                }
                """.formatted(LocalDate.now().format(FORMATTER));

        String promoPayload2 = """
                {
                  "user_id": 2,
                  "date": "%s",
                  "product": {
                    "product_id": 2,
                    "product_name": "Mouse",
                    "type": "Tech",
                    "brand": "Razer",
                    "color": "Green",
                    "notes": "Gaming"
                  },
                  "category": 100,
                  "price": 80.0,
                  "has_promo": true,
                  "discount": 0.20
                }
                """.formatted(LocalDate.now().format(FORMATTER));

        mockMvc.perform(post("/products/promo-pub")
                .contentType(MediaType.APPLICATION_JSON)
                .content(promoPayload1));

        mockMvc.perform(post("/products/promo-pub")
                .contentType(MediaType.APPLICATION_JSON)
                .content(promoPayload2));

        mockMvc.perform(get("/products/promo-pub/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Todos"))
                .andExpect(jsonPath("$.promoProductsCount").value(2));
    }

    @Test
    void shouldDeleteAllData() throws Exception {
        mockMvc.perform(delete("/users/all"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldDeleteAllDataIncludingPosts() throws Exception {
        String postPayload = """
                {
                  "user_id": 1,
                  "date": "%s",
                  "product": {
                    "product_id": 1,
                    "product_name": "Mouse",
                    "type": "Tech",
                    "brand": "Logi",
                    "color": "Black",
                    "notes": "Wireless"
                  },
                  "category": 100,
                  "price": 50.0,
                  "has_promo": false
                }
                """.formatted(LocalDate.now().format(FORMATTER));

        mockMvc.perform(post("/products/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postPayload));

        mockMvc.perform(delete("/users/all"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
