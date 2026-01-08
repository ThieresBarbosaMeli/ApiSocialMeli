package com.example.apisocialmeli;

import com.example.apisocialmeli.dto.UpdatePasswordRequest;
import com.example.apisocialmeli.dto.UpdateUserProfileRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateUserRequest user(int id, String name, String email) {
        CreateUserRequest request = new CreateUserRequest();
        request.setId(id);
        request.setName(name);
        request.setEmail(email);
        request.setPassword("123456");
        return request;
    }

    @BeforeEach
    void setUp() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user(1, "Alice", "alice@test.com"))));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user(2, "Bob", "bob@test.com"))));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user(3, "Charlie", "charlie@test.com"))));
    }

    @Test
    void shouldFollowUserAndCountFollowers_US0001_US0002() throws Exception {
        mockMvc.perform(post("/users/1/follow/2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/2/followers/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.userName").value("Bob"))
                .andExpect(jsonPath("$.followersCount").value(1));
    }

    @Test
    void shouldListFollowersOrderedByName_US0003_US0008() throws Exception {
        mockMvc.perform(post("/users/1/follow/2"));
        mockMvc.perform(post("/users/3/follow/2"));

        mockMvc.perform(get("/users/2/followers/list")
                        .param("order", "name_asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.followers[0].userName").value("Alice"))
                .andExpect(jsonPath("$.followers[1].userName").value("Charlie"));

        mockMvc.perform(get("/users/2/followers/list")
                        .param("order", "name_desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followers[0].userName").value("Charlie"))
                .andExpect(jsonPath("$.followers[1].userName").value("Alice"));
    }

    @Test
    void shouldListFollowedOrderedByName_US0004_US0008() throws Exception {
        mockMvc.perform(post("/users/2/follow/1"));
        mockMvc.perform(post("/users/2/follow/3"));

        mockMvc.perform(get("/users/2/followed/list")
                        .param("order", "name_asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.followed[0].userName").value("Alice"))
                .andExpect(jsonPath("$.followed[1].userName").value("Charlie"));

        mockMvc.perform(get("/users/2/followed/list")
                        .param("order", "name_desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followed[0].userName").value("Charlie"))
                .andExpect(jsonPath("$.followed[1].userName").value("Alice"));
    }

    @Test
    void shouldUnfollowUser_US0007() throws Exception {
        mockMvc.perform(post("/users/1/follow/2"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/1/unfollow/2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/2/followers/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followersCount").value(0));
    }

    @Test
    void shouldUpdateProfileAndPassword() throws Exception {
        UpdateUserProfileRequest profileRequest = new UpdateUserProfileRequest();
        profileRequest.setName("Alice Atualizada");
        profileRequest.setEmail("alice.new@test.com");

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk());

        UpdatePasswordRequest passwordRequest = new UpdatePasswordRequest();
        passwordRequest.setPassword("novaSenha!");

        mockMvc.perform(put("/users/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUserAndRemoveFromFollowers() throws Exception {
        mockMvc.perform(post("/users/1/follow/2"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/2/followers/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followersCount").value(0));
    }
}