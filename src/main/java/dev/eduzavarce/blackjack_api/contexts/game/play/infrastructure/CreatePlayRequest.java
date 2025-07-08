package dev.eduzavarce.blackjack_api.contexts.game.play.infrastructure;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

import dev.eduzavarce.blackjack_api.contexts.shared.infrastructure.ValidUUID;

@Schema(description = "Request object for creating a new play")
public record CreatePlayRequest(
    @Schema(
            description = "ID of the user creating the play",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        @ValidUUID
        String userId,
    @Schema(
            description = "Amount to bet on this play (must be positive and max 100)",
            example = "50.0",
            minimum = "0.01",
            maximum = "100")
        @Positive(message = "Bet amount must be positive") @Max(100)
        double betAmount) {}
