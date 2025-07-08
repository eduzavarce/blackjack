package dev.eduzavarce.blackjack_api.contexts.game.deck.domain;

public enum Suit {
  HEARTS("Hearts"),
  DIAMONDS("Diamonds"),
  CLUBS("Clubs"),
  SPADES("Spades");

  private final String name;

  Suit(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
