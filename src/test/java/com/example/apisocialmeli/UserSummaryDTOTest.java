package com.example.apisocialmeli;

import com.example.apisocialmeli.dto.response.UserSummaryDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserSummaryDTOTest {

    @Test
    void shouldPopulateFields() {
        UserSummaryDTO dto = new UserSummaryDTO(1, "Alice");
        assertEquals(1, dto.getUserId());
        assertEquals("Alice", dto.getUserName());
    }
}
