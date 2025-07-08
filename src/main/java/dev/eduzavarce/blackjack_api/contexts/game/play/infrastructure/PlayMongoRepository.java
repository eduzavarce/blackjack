package dev.eduzavarce.blackjack_api.contexts.game.play.infrastructure;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.game.play.domain.Play;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayEntity;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayRepository;

@Repository
public interface PlayMongoRepository
    extends ReactiveMongoRepository<PlayMongoEntity, String>, PlayRepository {

  @Override
  default Mono<Play> findPlayById(String id) {
    return findById(id).map(PlayMongoEntity::toDomain);
  }

  @Override
  default Mono<PlayEntity> save(Play play) {
    PlayMongoEntity entity = PlayMongoEntity.fromDomain(play);
    return save(entity).cast(PlayEntity.class);
  }
}
