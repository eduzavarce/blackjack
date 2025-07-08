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
            description = "Value of the card (1-13, where Ace=1, Jack=11, Queen=12, King=13)",
            example = "10")
        int value) {

  @Override
  public String toString() {
    return value + " of " + suit;
  }
}
