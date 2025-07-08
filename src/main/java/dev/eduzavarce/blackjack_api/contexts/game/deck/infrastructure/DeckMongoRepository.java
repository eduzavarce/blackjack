package dev.eduzavarce.blackjack_api.contexts.game.deck.infrastructure;

import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.DeckRepository;

@Primary
@Repository
public interface DeckMongoRepository
    extends ReactiveMongoRepository<DeckMongoEntity, String>, DeckRepository {}
