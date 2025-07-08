package dev.eduzavarce.blackjack_api.contexts.auth.user.domain;

/**
 * Exception thrown when a user does not exist. This exception is thrown when attempting to find a
 * user by ID and the user does not exist.
 */
public class UserDoesNotExistException extends RuntimeException {
  private final String userId;

  /**
   * Constructor for UserDoesNotExistException.
   *
   * @param userId The ID of the user that does not exist
   */
  public UserDoesNotExistException(String userId) {
    super("User with ID " + userId + " does not exist");
    this.userId = userId;
  }

  /**
   * Get the ID of the user that does not exist.
   *
   * @return The user ID
   */
  public String getUserId() {
    return userId;
  }
}
