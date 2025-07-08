package dev.eduzavarce.blackjack_api.contexts.game.deck.domain;

import java.util.*;
import java.util.stream.Collectors;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.AggregateRoot;

public class Deck extends AggregateRoot {
  private final List<Card> cards;
  private final Random random;
  private final String id;

  private Deck(String id) {
    this(new Random(), id);
  }

  private Deck(Random random, String id) {
    this.id = id;
    this.random = random;
    this.cards = buildDeck();
    shuffle();
  }

  public static Deck create(String id) {
    return new Deck(id);
  }

  public static Deck fromPrimitives(Map<String, Object> primitives) {
    String id = (String) primitives.get("id");
    Deck deck = new Deck(id);
    deck.cards.clear();

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> cardsList = (List<Map<String, Object>>) primitives.get("cards");

    for (Map<String, Object> cardMap : cardsList) {
      String suit = (String) cardMap.get("suit");
      int value = ((Number) cardMap.get("value")).intValue();
      deck.cards.add(new Card(suit, value));
    }

    return deck;
  }

  private List<Card> buildDeck() {
    List<Card> deck = new ArrayList<>();
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        deck.add(new Card(suit.getName(), rank.getValue()));
      }
    }
    // Shuffle the deck
    return deck;
  }

  private void shuffle() {
    Collections.shuffle(cards, random);
  }

  public Card drawCard() {
    if (cards.isEmpty()) {
      throw new EmptyDeckException("No cards left in the deck");
    }
    return cards.removeLast();
  }

  public Map<String, Object> toPrimitives() {
    Map<String, Object> primitives = new HashMap<>();
    primitives.put("id", this.id);
    primitives.put(
        "cards",
        this.cards.stream()
            .map(
                card ->
                    Map.of(
                        "suit", card.suit(),
                        "value", card.value()))
            .collect(Collectors.toList()));
    return primitives;
  }
}
