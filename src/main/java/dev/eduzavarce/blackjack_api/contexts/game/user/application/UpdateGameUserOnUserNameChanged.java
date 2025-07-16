package dev.eduzavarce.blackjack_api.contexts.game.user.application;

import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserNameChanged;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUser;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserCreate;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserEntity;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserRepository;
import dev.eduzavarce.blackjack_api.contexts.game.user.infrastructure.GameUserMongoEntity;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEventSubscriber;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;
import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.DatabaseException;
import org.springframework.stereotype.Service;

@Service
@DomainEventSubscriber({UserNameChanged.class})
public class UpdateGameUserOnUserNameChanged {
    private final GameUserRepository gameUserRepository;

    public UpdateGameUserOnUserNameChanged(GameUserRepository gameUserRepository) {
        this.gameUserRepository = gameUserRepository;

    }

    public void on(UserNameChanged event) {
        try {
            UserDto dto = (UserDto) event.toPrimitives().body();
            updateGameUser(dto);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void updateGameUser(UserDto dto) {
        gameUserRepository
                .findById(dto.id())
                .flatMap(
                        gameUserEntity -> {
                            GameUser gameUser = gameUserEntity.toDomain();

                            // Create a new GameUser with the updated name
                            GameUserCreate updatedGameUserCreate =
                                    new GameUserCreate(
                                            dto.id(),
                                            dto.name(),
                                            gameUser.toPrimitives().balance(),
                                            gameUser.toPrimitives().blackjackGames());

                            GameUser updatedGameUser = GameUser.create(updatedGameUserCreate);

                            GameUserEntity updatedEntity = GameUserMongoEntity.fromDomain(updatedGameUser);
                            return gameUserRepository.save(updatedEntity);
                        })
                .subscribe();
    }
}
