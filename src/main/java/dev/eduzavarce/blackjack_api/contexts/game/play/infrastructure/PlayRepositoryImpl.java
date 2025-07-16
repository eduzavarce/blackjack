package dev.eduzavarce.blackjack_api.contexts.game.play.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.game.play.domain.Play;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayEntity;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayRepository;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.EventBus;

@Component
@Primary
public class PlayRepositoryImpl implements PlayRepository {
    private final PlayMongoRepository repository;
    private final EventBus eventBus;

    @Autowired
    public PlayRepositoryImpl(PlayMongoRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<PlayEntity> save(Play play) {
        // First save the play
        return repository.save(play)
                .doOnSuccess(entity -> {
                    // Then publish any domain events
                    eventBus.publish(play.pullDomainEvents());
                });
    }

    @Override
    public Mono<Play> findPlayById(String id) {
        return repository.findPlayById(id);
    }
}