package dev.eduzavarce.blackjack_api.contexts.accounts.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccount;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountEntity;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountRepository;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayCompleted;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayDto;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEventSubscriber;

@Service
@DomainEventSubscriber({PlayCompleted.class})
public class UpdateAccountOnPlayCompleted {
  private final UserAccountRepository accountRepository;

  @Autowired
  public UpdateAccountOnPlayCompleted(UserAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public void handle(PlayCompleted event) {
    PlayDto dto = (PlayDto) event.toPrimitives().body();
    if (dto == null || dto.userId() == null) {
      throw new IllegalArgumentException("PlayDto or userId cannot be null");
    }

    // Only update the account if the player won
    if (!dto.status().isWon()) {
      return; // Player lost, no need to update the account
    }

    // Update the account based on the PlayCompleted event
    accountRepository
        .findById(dto.userId())
        .switchIfEmpty(
            Mono.error(
                new IllegalArgumentException("User account not found for userId: " + dto.userId())))
        .flatMap(
            userAccountEntity -> {
              UserAccount userAccount = userAccountEntity.toDomain();
              // If the player won, add twice the bet amount (original bet + winnings)
              userAccount.increaseFunds(dto.betAmount() * 2);
              return accountRepository.save(UserAccountEntity.fromDomain(userAccount));
            })
        .subscribe();
  }
}
