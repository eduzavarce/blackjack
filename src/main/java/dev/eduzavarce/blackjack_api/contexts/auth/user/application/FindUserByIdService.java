package dev.eduzavarce.blackjack_api.contexts.auth.user.application;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.User;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserDoesNotExistException;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserEntity;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserRepository;

@Service
public class FindUserByIdService {

  private final UserRepository userRepository;

  public FindUserByIdService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Mono<User> execute(String id) {
    return userRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new UserDoesNotExistException(id)))
        .map(UserEntity::toDomain);
  }
}
