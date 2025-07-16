package dev.eduzavarce.blackjack_api.contexts.game.play.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.game.play.domain.Play;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayDto;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayRepository;

@Service
public class PerformPlayService {
  private final PlayRepository playRepository;

  @Autowired
  public PerformPlayService(PlayRepository playRepository) {
    this.playRepository = playRepository;
  }

  public Mono<PlayDto> execute(String playId) {
    return playRepository
        .findPlayById(playId)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Play not found")))
        .flatMap(
            play -> {
              // Draw a card for the player
              Play updatedPlay = play.performPlay();

              // If the player hasn't busted, let the dealer play
              if (updatedPlay.toPrimitives().status().isInProgress()) {
                updatedPlay = updatedPlay.performDealerPlay();
              }

              // Save the updated play
              return playRepository.save(updatedPlay).thenReturn(updatedPlay.toPrimitives());
            });
  }

  public Mono<PlayDto> executeStand(String playId) {
    return playRepository
        .findPlayById(playId)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Play not found")))
        .flatMap(
            play -> {
              // Player stands, dealer plays
              Play updatedPlay = play.performStand();

              // Save the updated play
              return playRepository.save(updatedPlay).thenReturn(updatedPlay.toPrimitives());
            });
  }
}
