package dev.eduzavarce.blackjack_api.contexts.auth.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.AlreadyExistsException;

/**
 * Exception thrown when attempting to create a user that already exists. This exception is thrown
 * when attempting to create a user with an ID that already exists.
 */
public class UserAlreadyExistsException extends AlreadyExistsException {
  private final String userId;

  /**
   * Constructor for UserAlreadyExistsException.
   *
   * @param userId The ID of the user that already exists
   */
  public UserAlreadyExistsException(String userId) {
    super("User with ID " + userId + " already exists");
    this.userId = userId;
  }

  /**
   * Get the ID of the user that already exists.
   *
   * @return The user ID
   */
  public String getUserId() {
    return userId;
  }
}
