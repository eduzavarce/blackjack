package dev.eduzavarce.blackjack_api.contexts.auth.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.Entity;

public interface UserEntity extends Entity<User> {

  static UserEntity fromDomain(User user) {
    throw new UnsupportedOperationException(
        "This method should be implemented by concrete classes");
  }

  User toDomain();
}
