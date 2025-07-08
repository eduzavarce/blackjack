package dev.eduzavarce.blackjack_api.contexts.game.user.domain;

import reactor.core.publisher.Mono;

public interface GameUserRepository {
  Mono<? extends GameUserEntity> save(GameUserEntity gameUser);

  Mono<? extends GameUserEntity> findById(String id);

  Mono<Void> deleteById(String id);
}
