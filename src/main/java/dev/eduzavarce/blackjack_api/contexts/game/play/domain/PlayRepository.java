package dev.eduzavarce.blackjack_api.contexts.game.play.domain;

import reactor.core.publisher.Mono;

public interface PlayRepository {
  Mono<? extends PlayEntity> save(Play play);

  Mono<Play> findPlayById(String id);
}
