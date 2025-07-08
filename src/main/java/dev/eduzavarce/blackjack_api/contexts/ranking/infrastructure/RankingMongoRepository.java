package dev.eduzavarce.blackjack_api.contexts.ranking.infrastructure;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.ranking.domain.Ranking;
import dev.eduzavarce.blackjack_api.contexts.ranking.domain.RankingDto;
import dev.eduzavarce.blackjack_api.contexts.ranking.domain.RankingRepository;

@Repository
public interface RankingMongoRepository
    extends ReactiveMongoRepository<RankingMongoEntity, String>, RankingRepository {

  @Query(sort = "{ earnings: -1 }")
  Flux<RankingMongoEntity> findAllByOrderByEarningsDesc();

  @Override
  default Mono<Ranking> findByUserId(String userId) {
    return findById(userId)
        .map(RankingMongoEntity::toDomain)
        .switchIfEmpty(Mono.just(Ranking.create(userId)));
  }

  @Override
  default Mono<Void> save(Ranking ranking) {
    return save(RankingMongoEntity.fromDomain(ranking)).then();
  }

  @Override
  default Flux<RankingDto> findTopRankings(int limit) {
    return findAllByOrderByEarningsDesc()
        .take(limit)
        .map(entity -> entity.toDomain().toPrimitives());
  }
}
