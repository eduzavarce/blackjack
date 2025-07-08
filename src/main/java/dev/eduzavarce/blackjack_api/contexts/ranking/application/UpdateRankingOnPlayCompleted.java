package dev.eduzavarce.blackjack_api.contexts.ranking.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayCompleted;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayDto;
import dev.eduzavarce.blackjack_api.contexts.ranking.domain.RankingRepository;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEventSubscriber;

@Service
@DomainEventSubscriber({PlayCompleted.class})
public class UpdateRankingOnPlayCompleted {
  private final RankingRepository rankingRepository;

  @Autowired
  public UpdateRankingOnPlayCompleted(RankingRepository rankingRepository) {
    this.rankingRepository = rankingRepository;
  }

  public void handle(PlayCompleted event) {
    PlayDto dto = (PlayDto) event.toPrimitives().body();
    if (dto == null || dto.userId() == null) {
      throw new IllegalArgumentException("PlayDto or userId cannot be null");
    }

    // Update the ranking based on the PlayCompleted event
    rankingRepository
        .findByUserId(dto.userId())
        .flatMap(
            ranking -> {
              // Update wins/losses and earnings
              if (dto.status().isWon()) {
                ranking.addWin(dto.betAmount());
              } else if (dto.status().isLost()) {
                ranking.addLoss(dto.betAmount());
              }

              // Save the updated ranking
              return rankingRepository.save(ranking);
            })
        .subscribe();
  }
}
