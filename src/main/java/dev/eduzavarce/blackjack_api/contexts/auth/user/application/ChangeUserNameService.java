package dev.eduzavarce.blackjack_api.contexts.auth.user.application;

import java.util.List;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.User;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserDoesNotExistException;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserEntity;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserRepository;
import dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure.UserMongoEntity;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEvent;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.EventBus;

@Service
public class ChangeUserNameService {

  private final UserRepository userRepository;
  private final EventBus eventBus;

  public ChangeUserNameService(UserRepository userRepository, EventBus eventBus) {
    this.userRepository = userRepository;
    this.eventBus = eventBus;
  }

  public Mono<Void> execute(String id, String newName) {
    return userRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new UserDoesNotExistException(id)))
        .flatMap(
            userEntity -> {
              User user = userEntity.toDomain();
              user.changeName(newName);

              List<DomainEvent> events = user.pullDomainEvents();

              UserEntity updatedEntity = UserMongoEntity.fromDomain(user);
              return userRepository
                  .save(updatedEntity)
                  .doOnSuccess(
                      savedEntity -> {
                        eventBus.publish(events);
                      });
            })
        .then();
  }
}
