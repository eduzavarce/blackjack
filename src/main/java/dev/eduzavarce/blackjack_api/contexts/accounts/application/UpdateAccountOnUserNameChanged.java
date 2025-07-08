package dev.eduzavarce.blackjack_api.contexts.accounts.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccount;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountEntity;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountRepository;
import dev.eduzavarce.blackjack_api.contexts.accounts.infrastructure.UserAccountMySqlEntity;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserNameChanged;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEventSubscriber;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;
import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.DatabaseException;

@Service
@DomainEventSubscriber({UserNameChanged.class})
public class UpdateAccountOnUserNameChanged {
  private static final Logger logger =
      LoggerFactory.getLogger(UpdateAccountOnUserNameChanged.class);
  private final UserAccountRepository userAccountRepository;

  public UpdateAccountOnUserNameChanged(UserAccountRepository userAccountRepository) {
    this.userAccountRepository = userAccountRepository;
    logger.info(
        "UpdateAccountOnUserNameChanged initialized with repository: {}",
        userAccountRepository.getClass().getSimpleName());
  }

  public void on(UserNameChanged event) {
    logger.info("Received UserNameChanged event: {}", event.eventId());
    try {
      UserDto dto = (UserDto) event.toPrimitives().body();
      logger.info("Extracted UserDto from event: id={}, name={}", dto.id(), dto.name());
      updateUserAccount(dto);
    } catch (Exception e) {
      logger.error("Error processing UserNameChanged event", e);
      throw new DatabaseException(e.getMessage());
    }
  }

  private void updateUserAccount(UserDto dto) {
    userAccountRepository
        .findById(dto.id())
        .flatMap(
            userAccountEntity -> {
              UserAccount userAccount = userAccountEntity.toDomain();

              // Create a new UserAccount with the updated name
              UserAccount updatedUserAccount = UserAccount.create(dto);

              UserAccountEntity updatedEntity =
                  UserAccountMySqlEntity.fromDomain(updatedUserAccount, false);
              return userAccountRepository.save(updatedEntity);
            })
        .subscribe();
  }
}
