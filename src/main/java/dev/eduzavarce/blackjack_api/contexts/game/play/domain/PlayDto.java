package dev.eduzavarce.blackjack_api.contexts.game.play.domain;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.Card;

@Schema(description = "Data transfer object for a blackjack play")
public record PlayDto(
    @Schema(
            description = "Unique identifier for the play",
            example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
    @Schema(description = "Current status of the play", example = "IN_PROGRESS") PlayStatus status,
    @Schema(
            description = "ID of the user who created the play",
            example = "123e4567-e89b-12d3-a456-426614174001")
        String userId,
    @Schema(description = "Amount bet on this play", example = "100.0") double betAmount,
    @Schema(description = "Cards in the player's hand") List<Card> playerCards,
    @Schema(description = "Cards in the dealer's hand") List<Card> dealerCards,
    @Schema(description = "Current score of the player's hand", example = "17") int playerScore,
    @Schema(description = "Current score of the dealer's hand", example = "19") int dealerScore) {
  // Constructor with only the original fields for backward compatibility
  public PlayDto(String id, PlayStatus status, String userId, double betAmount) {
    this(id, status, userId, betAmount, null, null, 0, 0);
  }
}
