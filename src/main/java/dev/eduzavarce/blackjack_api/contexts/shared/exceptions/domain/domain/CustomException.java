package dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain;

public class CustomException extends RuntimeException {
  public CustomException(String message) {
    super(message);
  }
}
