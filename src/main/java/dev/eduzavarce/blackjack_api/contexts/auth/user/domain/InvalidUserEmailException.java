package dev.eduzavarce.blackjack_api.contexts.auth.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.CustomException;

public class InvalidUserEmailException extends CustomException {
  public InvalidUserEmailException(String message) {
    super(message);
  }
}
