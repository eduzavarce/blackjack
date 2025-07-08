package dev.eduzavarce.blackjack_api.contexts.game.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.Entity;

public interface GameUserEntity extends Entity<GameUser> {

  static GameUserEntity fromDomain(GameUser gameUser) {
    throw new UnsupportedOperationException(
        "This method should be implemented by concrete classes");
  }

  GameUser toDomain();
}
