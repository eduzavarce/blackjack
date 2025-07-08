package dev.eduzavarce.blackjack_api.contexts.game.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.CustomException;

public class InvalidGameUserNameException extends CustomException {
  public InvalidGameUserNameException(String message) {
    super(message);
  }
}
