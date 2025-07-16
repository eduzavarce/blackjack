package dev.eduzavarce.blackjack_api.contexts.game.deck.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Playing card in a deck")
public record Card(
    @Schema(
            description = "Suit of the card",
            example = "Hearts",
            allowableValues = {"Hearts", "Diamonds", "Clubs", "Spades"})
        String suit,
    @Schema(
            description = "Value of the card (1-10, where Ace=1, Jack/Queen/King=10)",
            example = "10")
        int value,
    @Schema(
            description = "Rank of the card",
            example = "KING",
            allowableValues = {
              "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK",
              "QUEEN", "KING", "ACE"
            })
        Rank rank) {

  // Constructor that takes only suit and value for backward compatibility
  public Card(String suit, int value) {
    this(suit, value, null);
  }

  @Override
  public String toString() {
    return (rank != null ? rank.name() : value) + " of " + suit;
  }
}
