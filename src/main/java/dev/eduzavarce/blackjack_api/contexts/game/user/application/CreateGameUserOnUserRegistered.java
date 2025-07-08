package dev.eduzavarce.blackjack_api.contexts.game.user.application;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserRegistered;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUser;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserCreate;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserEntity;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserRepository;
import dev.eduzavarce.blackjack_api.contexts.game.user.infrastructure.GameUserMongoEntity;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEventSubscriber;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;
import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.DatabaseException;

@Service
@DomainEventSubscriber({UserRegistered.class})
public class CreateGameUserOnUserRegistered {

  private final GameUserRepository gameUserRepository;

  public CreateGameUserOnUserRegistered(GameUserRepository gameUserRepository) {
    this.gameUserRepository = gameUserRepository;
  }

  public void on(UserRegistered event) {
    try {
      UserDto userDto = (UserDto) event.toPrimitives().body();

      createGameContextUser(userDto);

    } catch (Exception e) {
      throw new DatabaseException(e.getMessage());
    }
  }

  private void createGameContextUser(UserDto userDto) {
    GameUserCreate gameUserCreate =
        new GameUserCreate(userDto.id(), userDto.name(), userDto.balance(), new ArrayList<>());

    GameUser gameUser = GameUser.create(gameUserCreate);

    GameUserEntity gameUserEntity = GameUserMongoEntity.fromDomain(gameUser);

    gameUserRepository.save(gameUserEntity).subscribe();
  }
}
