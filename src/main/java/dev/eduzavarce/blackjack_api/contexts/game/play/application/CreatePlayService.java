package dev.eduzavarce.blackjack_api.contexts.game.play.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountRepository;
import dev.eduzavarce.blackjack_api.contexts.game.deck.application.CreateDeckService;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.CreatePlayDto;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.Play;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayRepository;

@Service
public class CreatePlayService {
  private final CreateDeckService createDeckService;
  private final PlayRepository playRepository;
  private final UserAccountRepository userAccountRepository;

  @Autowired
  public CreatePlayService(
      CreateDeckService createDeckService,
      PlayRepository playRepository,
      UserAccountRepository userAccountRepository) {
    this.createDeckService = createDeckService;
    this.playRepository = playRepository;
    this.userAccountRepository = userAccountRepository;
  }

  public Mono<Void> execute(CreatePlayDto dto) {
    // Validate bet amount is not negative
    if (dto.betAmount() < 0) {
      return Mono.error(new IllegalArgumentException("Bet amount cannot be negative"));
    }

    // Check if user has enough balance
    return userAccountRepository
        .findById(dto.userId())
        .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found")))
        .flatMap(
            userAccountEntity -> {
              double userBalance = userAccountEntity.toDomain().toPrimitives().balance();
              if (dto.betAmount() > userBalance) {
                return Mono.error(
                    new IllegalArgumentException("Bet amount cannot be greater than user balance"));
              }

              // Create and save the play
              Play play = Play.create(dto.id(), dto.userId(), dto.betAmount());
              return playRepository.save(play);
            })
        .then();
  }
}
