package com.example.apisocialmeli.dto.request;

import com.example.apisocialmeli.exception.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdatePasswordRequest {

    @NotBlank(message = ErrorMessages.UPDATE_PASSWORD_NOT_BLANK)
    @Size(min = 6, message = ErrorMessages.UPDATE_PASSWORD_SIZE)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}