package dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure;

import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.Rank;

public class CardDocument {
  private String suit;
  private int value;
  private String rank;

  public CardDocument() {}

  public CardDocument(String suit, int value) {
    this.suit = suit;
    this.value = value;
  }

  public CardDocument(String suit, int value, String rank) {
    this.suit = suit;
    this.value = value;
    this.rank = rank;
  }

  public CardDocument(String suit, int value, Rank rank) {
    this.suit = suit;
    this.value = value;
    this.rank = rank != null ? rank.name() : null;
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

  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }
}
