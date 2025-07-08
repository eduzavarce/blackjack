package dev.eduzavarce.blackjack_api.contexts.accounts.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.Entity;

public interface UserAccountEntity extends Entity<UserAccount> {

  static UserAccountEntity fromDomain(UserAccount userAccount) {
    throw new UnsupportedOperationException(
        "This method should be implemented by concrete classes");
  }

  UserAccount toDomain();
}
