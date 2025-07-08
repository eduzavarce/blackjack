package dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain;

public class EmptyDeckException extends CustomException {
  public EmptyDeckException(String message) {
    super(message);
  }
}
