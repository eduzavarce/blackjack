package dev.eduzavarce.blackjack_api.contexts.accounts.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccount;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountEntity;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountRepository;
import dev.eduzavarce.blackjack_api.contexts.accounts.infrastructure.UserAccountMySqlEntity;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserRegistered;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEventSubscriber;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;
import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.DatabaseException;

@Service
@DomainEventSubscriber({UserRegistered.class})
public class CreateAccountOnUserRegistered {
  private static final Logger logger = LoggerFactory.getLogger(CreateAccountOnUserRegistered.class);
  private final UserAccountRepository userAccountRepository;

  public CreateAccountOnUserRegistered(UserAccountRepository userAccountRepository) {
    this.userAccountRepository = userAccountRepository;
    logger.info(
        "OnUserRegistered initialized with repository: {}",
        userAccountRepository.getClass().getSimpleName());
  }

  public void on(UserRegistered event) {
    logger.info("Received UserRegistered event: {}", event.eventId());
    try {
      UserDto dto = (UserDto) event.toPrimitives().body();
      logger.info("Extracted UserDto from event: id={}, name={}", dto.id(), dto.name());
      createUserAccount(dto);
    } catch (Exception e) {
      logger.error("Error processing UserRegistered event", e);
      throw new DatabaseException(e.getMessage());
    }
  }

  private void createUserAccount(UserDto dto) {
    UserAccount userAccount = UserAccount.create(dto);

    UserAccountEntity userAccountEntity = UserAccountMySqlEntity.fromDomain(userAccount, true);

    userAccountRepository
        .findById(dto.id())
        .hasElement()
        .flatMap(
            exists -> {
              if (exists) {
                return Mono.empty();
              } else {
                return userAccountRepository.save(userAccountEntity);
              }
            })
        .subscribe();
  }
}
