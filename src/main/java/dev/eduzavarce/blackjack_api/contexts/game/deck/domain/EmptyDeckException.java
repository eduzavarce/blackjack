package dev.eduzavarce.blackjack_api.contexts.game.deck.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.CustomException;

public class EmptyDeckException extends CustomException {
  public EmptyDeckException(String s) {
    super(s);
  }
}
