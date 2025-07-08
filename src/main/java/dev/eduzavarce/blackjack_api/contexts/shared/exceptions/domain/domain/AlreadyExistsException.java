package dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain;

public class AlreadyExistsException extends CustomException {
  public AlreadyExistsException(String message) {
    super(message);
  }
}
