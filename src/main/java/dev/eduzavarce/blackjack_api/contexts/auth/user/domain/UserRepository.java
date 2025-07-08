package dev.eduzavarce.blackjack_api.contexts.auth.user.domain;

import reactor.core.publisher.Mono;

/**
 * Repository interface for User entities. This interface defines the contract for repositories that
 * handle User entities.
 */
public interface UserRepository {

  Mono<? extends UserEntity> save(UserEntity user);

  Mono<? extends UserEntity> findById(String id);

  Mono<? extends UserEntity> findByEmail(String email);

  Mono<Void> deleteById(String id);
}
