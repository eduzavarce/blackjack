package dev.eduzavarce.blackjack_api.contexts.auth.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.StringValueObject;

public class UserName extends StringValueObject {
  public UserName(String value) {
    super(value);
    validate(value);
  }

  private void validate(String value) {
    if (value == null || value.trim().isEmpty() || value.length() > 50) {
      throw new InvalidUserNameException("Invalid user name: " + value);
    }
  }
}
