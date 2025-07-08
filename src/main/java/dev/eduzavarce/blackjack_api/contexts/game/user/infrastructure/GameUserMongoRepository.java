package dev.eduzavarce.blackjack_api.contexts.game.user.infrastructure;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserRepository;

@Repository
public interface GameUserMongoRepository
    extends ReactiveMongoRepository<GameUserMongoEntity, String>, GameUserRepository {
  // The save and findById methods from ReactiveMongoRepository already satisfy the
  // GameUserRepository interface
  // because GameUserMongoEntity implements GameUserEntity

  // We only need to implement the methods that are not already provided by ReactiveMongoRepository
}
