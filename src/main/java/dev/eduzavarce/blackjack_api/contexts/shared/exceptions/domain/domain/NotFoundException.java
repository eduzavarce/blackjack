package dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain;

public class NotFoundException extends CustomException {
  public NotFoundException(String message) {
    super(message);
  }
}
