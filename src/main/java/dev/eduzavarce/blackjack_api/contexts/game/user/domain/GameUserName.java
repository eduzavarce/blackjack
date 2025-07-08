package dev.eduzavarce.blackjack_api.contexts.game.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.StringValueObject;

public class GameUserName extends StringValueObject {
  public GameUserName(String value) {
    super(value);
    validate(value);
  }

  private void validate(String value) {
    if (value == null || value.trim().isEmpty() || value.length() > 50) {
      throw new InvalidGameUserNameException("Invalid user name: " + value);
    }
  }
}
