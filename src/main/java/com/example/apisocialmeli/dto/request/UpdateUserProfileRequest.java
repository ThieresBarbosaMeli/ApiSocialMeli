package com.example.apisocialmeli.dto.request;

import com.example.apisocialmeli.exception.ErrorMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserProfileRequest {

    @NotBlank(message = ErrorMessages.UPDATE_NAME_NOT_BLANK)
    @Size(max = 40, message = ErrorMessages.UPDATE_NAME_SIZE)
    private String name;

    @NotBlank(message = ErrorMessages.UPDATE_EMAIL_NOT_BLANK)
    @Email(message = ErrorMessages.UPDATE_EMAIL_VALID)
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}