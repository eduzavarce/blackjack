package dev.eduzavarce.blackjack_api.contexts.ranking.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RankingRepository {
  Mono<Ranking> findByUserId(String userId);

  Mono<Void> save(Ranking ranking);

  Flux<RankingDto> findTopRankings(int limit);
}
