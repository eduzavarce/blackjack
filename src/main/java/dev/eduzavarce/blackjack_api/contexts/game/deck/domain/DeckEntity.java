package dev.eduzavarce.blackjack_api.contexts.game.deck.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.Entity;

public interface DeckEntity extends Entity<Deck> {

  static DeckEntity fromDomain(Deck deck) {
    throw new UnsupportedOperationException(
        "This method should be implemented by concrete classes");
  }

  Deck toDomain();
}
