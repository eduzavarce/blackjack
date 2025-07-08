package dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain;

public class EmptyFieldException extends CustomException {
  public EmptyFieldException(String message) {
    super(message);
  }
}
