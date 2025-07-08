package dev.eduzavarce.blackjack_api.contexts.accounts.domain;

import reactor.core.publisher.Mono;

public interface UserAccountRepository {
  Mono<? extends UserAccountEntity> save(UserAccountEntity userAccount);

  Mono<? extends UserAccountEntity> findById(String id);

  Mono<Void> deleteById(String id);
}
