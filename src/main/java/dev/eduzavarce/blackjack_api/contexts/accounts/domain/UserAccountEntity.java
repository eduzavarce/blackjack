package dev.eduzavarce.blackjack_api.contexts.accounts.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.Entity;

public interface UserAccountEntity extends Entity<UserAccount> {

  static UserAccountEntity fromDomain(UserAccount userAccount) {
    return dev.eduzavarce.blackjack_api.contexts.accounts.infrastructure.UserAccountMySqlEntity.fromDomain(userAccount);
  }

  UserAccount toDomain();
}
