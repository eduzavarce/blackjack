package dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request object for creating a new user")
public record CreateUserRequest(
    @Schema(description = "User's full name", example = "John Doe", required = true)
        @NotBlank(message = "Name is required")
        String name,
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email)
    implements Serializable {}
