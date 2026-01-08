package com.example.apisocialmeli;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PostIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private String publishPayload(int postId, int userId, LocalDate date, boolean hasPromo, Double discount) {
        return """
                {
                  "id": %d,
                  "userId": %d,
                  "date": "%s",
                  "product": {
                    "productId": 10,
                    "productName": "Mouse Gamer",
                    "type": "Periférico",
                    "brand": "Logi",
                    "color": "Preto",
                    "notes": "RGB"
                  },
                  "category": 100,
                  "price": 250.0,
                  "hasPromo": %s,
                  "discount": %s
                }
                """.formatted(
                postId,
                userId,
                date.format(FORMATTER),
                hasPromo,
                discount == null ? "null" : discount.toString()
        );
    }

    @BeforeEach
    void setup() throws Exception {
        CreateUserRequest seller = new CreateUserRequest();
        seller.setId(10);
        seller.setName("Vendedor");
        seller.setEmail("seller@test.com");
        seller.setPassword("123456");

        CreateUserRequest buyer = new CreateUserRequest();
        buyer.setId(20);
        buyer.setName("Comprador");
        buyer.setEmail("buyer@test.com");
        buyer.setPassword("123456");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seller)));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buyer)));

        mockMvc.perform(post("/users/20/follow/10"));
    }

    @Test
    void shouldPublishPostAndGetFeed_US0005_US0006_US0009() throws Exception {
        mockMvc.perform(post("/posts/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(publishPayload(1, 10, LocalDate.now(), false, null)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/products/followed/20/list")
                        .param("order", "date_desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(20))
                .andExpect(jsonPath("$.posts.length()").value(1))
                .andExpect(jsonPath("$.posts[0].user_id").value(10))
                .andExpect(jsonPath("$.posts[0].product.product_name").value("Mouse Gamer"));
    }

    @Test
    void shouldReturnOnlyLastTwoWeeksInFeed_US0006() throws Exception {
        mockMvc.perform(post("/posts/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(publishPayload(1, 10, LocalDate.now().minusDays(1), false, null)));

        mockMvc.perform(post("/posts/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(publishPayload(2, 10, LocalDate.now().minusDays(20), false, null)));

        mockMvc.perform(get("/posts/products/followed/20/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts.length()").value(1))
                .andExpect(jsonPath("$.posts[0].post_id").value(1));
    }

    @Test
    void shouldRespectDateAscendingOrder_US0009() throws Exception {
        mockMvc.perform(post("/posts/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(publishPayload(1, 10, LocalDate.now().minusDays(1), false, null)));

        mockMvc.perform(post("/posts/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(publishPayload(2, 10, LocalDate.now(), false, null)));

        mockMvc.perform(get("/posts/products/followed/20/list")
                        .param("order", "date_asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[0].post_id").value(1))
                .andExpect(jsonPath("$.posts[1].post_id").value(2));
    }

    @Test
    void shouldPublishPromoAndGetPromoCount_US0010_US0011() throws Exception {
        mockMvc.perform(post("/posts/promo-publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(publishPayload(1, 10, LocalDate.now(), true, 0.2)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/posts/promo-publish/count")
                        .param("user_id", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.promoProductsCount").value(1));
    }

    @Test
    void shouldListPromoPosts_US0012() throws Exception {
        mockMvc.perform(post("/posts/promo-publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(publishPayload(1, 10, LocalDate.now(), true, 0.15)));

        mockMvc.perform(get("/posts/promo-publish/list")
                        .param("user_id", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts.length()").value(1))
                .andExpect(jsonPath("$.posts[0].has_promo").value(true))
                .andExpect(jsonPath("$.posts[0].discount").value(0.15));
    }
}