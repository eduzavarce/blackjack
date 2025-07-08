package dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure;

public class CardDocument {
  private String suit;
  private int value;

  public CardDocument() {}

  public CardDocument(String suit, int value) {
    this.suit = suit;
    this.value = value;
  }

  public String getSuit() {
    return suit;
  }

  public void setSuit(String suit) {
    this.suit = suit;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
