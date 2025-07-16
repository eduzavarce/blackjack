package dev.eduzavarce.blackjack_api.contexts.accounts.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccount;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountEntity;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountRepository;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayCreated;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayDto;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEventSubscriber;

@Service
@DomainEventSubscriber({PlayCreated.class})
public class UpdateAccountOnPlayCreated {
  private final UserAccountRepository accountRepository;

  @Autowired
  public UpdateAccountOnPlayCreated(UserAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public void on(PlayCreated event) {
    PlayDto dto = (PlayDto) event.toPrimitives().body();
    if (dto == null || dto.userId() == null) {
      throw new IllegalArgumentException("PlayDto or userId cannot be null");
    }

    accountRepository
        .findById(dto.userId())
        .switchIfEmpty(
            Mono.error(
                new IllegalArgumentException("User account not found for userId: " + dto.userId())))
        .flatMap(
            userAccountEntity -> {
              UserAccount userAccount = userAccountEntity.toDomain();
              userAccount.decreaseFunds(dto.betAmount());
              return accountRepository.save(UserAccountEntity.fromDomain(userAccount));
            })
        .subscribe();
  }
}
