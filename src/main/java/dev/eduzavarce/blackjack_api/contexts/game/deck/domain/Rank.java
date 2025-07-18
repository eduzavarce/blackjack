package dev.eduzavarce.blackjack_api.contexts.game.deck.domain;

public enum Rank {
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9),
  TEN(10),
  JACK(10),
  QUEEN(10),
  KING(10),
  ACE(1);

  private final int value;

  Rank(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
