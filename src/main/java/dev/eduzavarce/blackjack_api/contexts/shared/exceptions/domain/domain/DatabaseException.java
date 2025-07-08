package dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain;

public class DatabaseException extends CustomException {
  public DatabaseException(String message) {
    super(message);
  }
}
