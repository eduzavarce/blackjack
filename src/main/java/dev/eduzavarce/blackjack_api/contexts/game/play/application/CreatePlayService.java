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

    // Check if play already exists
    return playRepository
        .findPlayById(dto.id())
        .flatMap(existingPlay -> Mono.error(new IllegalArgumentException("Play already exists")))
        .switchIfEmpty(
            // Check if user has enough balance
            userAccountRepository
                .findById(dto.userId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found")))
                .flatMap(
                    userAccountEntity -> {
                      double userBalance = userAccountEntity.toDomain().toPrimitives().balance();
                      if (dto.betAmount() > userBalance) {
                        return Mono.error(
                            new IllegalArgumentException(
                                "Bet amount cannot be greater than user balance"));
                      }

                      // Create the play
                      Play play = Play.create(dto.id(), dto.userId(), dto.betAmount());

                      // Check if any player has reached 21
                      int playerScore = play.calculateHandValue(play.toPrimitives().playerCards());
                      int dealerScore = play.calculateHandValue(play.toPrimitives().dealerCards());

                      if (playerScore == 21 || dealerScore == 21) {
                        // Determine winner and mark as finished
                        play.performDealerPlay();
                      }

                      // Save the play
                      return playRepository.save(play);
                    }))
        .then();
  }
}
