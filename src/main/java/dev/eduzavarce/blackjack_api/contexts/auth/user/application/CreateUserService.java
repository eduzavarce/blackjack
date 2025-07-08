package dev.eduzavarce.blackjack_api.contexts.auth.user.application;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.User;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserEntity;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserRepository;
import dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure.UserMongoEntity;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.EventBus;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;
import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.AlreadyExistsException;

@Service
public class CreateUserService {
  private static final double SIGN_UP_BALANCE = 100.0;
  private final UserRepository userRepository;
  private final EventBus eventBus;

  public CreateUserService(UserRepository userRepository, EventBus eventBus) {
    this.userRepository = userRepository;
    this.eventBus = eventBus;
  }

  public Mono<Void> execute(String id, String name, String email) {

    return Mono.when(validateIdNotExists(id), validateEmailNotExists(email))
        .then(createAndSaveUser(id, name, email, SIGN_UP_BALANCE));
  }

  private Mono<Void> validateIdNotExists(String id) {
    return userRepository
        .findById(id)
        .flatMap(
            existingUser ->
                Mono.<Void>error(new AlreadyExistsException("User ID already exists: " + id)))
        .then();
  }

  private Mono<Void> validateEmailNotExists(String email) {
    return userRepository
        .findByEmail(email)
        .flatMap(
            existingUser ->
                Mono.<Void>error(new AlreadyExistsException("Email already exists: " + email)))
        .then();
  }

  private Mono<Void> createAndSaveUser(String id, String name, String email, double balance) {
    UserDto dto = new UserDto(id, name, email, balance);

    User user = User.create(dto);

    UserEntity userEntity = UserMongoEntity.fromDomain(user);
    return userRepository
        .save(userEntity)
        .doOnSuccess(
            savedEntity -> {
              eventBus.publish(user.pullDomainEvents());
            })
        .then();
  }
}
