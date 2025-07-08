package dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request object for changing a user's name")
public record ChangeUserNameRequest(
    @Schema(description = "New name for the user", example = "John Doe", required = true)
        @NotBlank(message = "Name cannot be blank")
        String name) {}
