package com.example.apisocialmeli.dto.request;

import com.example.apisocialmeli.exception.ErrorMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {

    @Positive(message = ErrorMessages.ID_POSITIVE)
    private int id;

    @NotBlank(message = ErrorMessages.NAME_NOT_BLANK)
    @Size(max = 15, message = ErrorMessages.NAME_SIZE)
    @Pattern(regexp = "[\\p{L}\\d ]+", message = ErrorMessages.NAME_PATTERN)
    private String name;

    @NotBlank(message = ErrorMessages.EMAIL_NOT_BLANK)
    @Email(message = ErrorMessages.EMAIL_VALID)
    private String email;

    @NotBlank(message = ErrorMessages.PASSWORD_NOT_BLANK)
    @Size(min = 6, max = 20, message = ErrorMessages.PASSWORD_SIZE)
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
