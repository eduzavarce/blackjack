package dev.eduzavarce.blackjack_api.contexts.auth.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.CustomException;

public class InvalidUserNameException extends CustomException {
  public InvalidUserNameException(String message) {
    super(message);
  }
}
