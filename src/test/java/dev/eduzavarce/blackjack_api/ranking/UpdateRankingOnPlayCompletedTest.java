package dev.eduzavarce.blackjack_api.ranking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayCompleted;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayDto;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayStatus;
import dev.eduzavarce.blackjack_api.contexts.ranking.application.UpdateRankingOnPlayCompleted;
import dev.eduzavarce.blackjack_api.contexts.ranking.domain.Ranking;
import dev.eduzavarce.blackjack_api.contexts.ranking.domain.RankingRepository;

@ExtendWith(MockitoExtension.class)
public class UpdateRankingOnPlayCompletedTest {

  @Mock private RankingRepository rankingRepository;

  private UpdateRankingOnPlayCompleted subscriber;

  @Captor private ArgumentCaptor<Ranking> rankingCaptor;

  private String playId;
  private String userId;
  private double betAmount;

  @BeforeEach
  void setUp() {
    subscriber = new UpdateRankingOnPlayCompleted(rankingRepository);
    playId = UUID.randomUUID().toString();
    userId = UUID.randomUUID().toString();
    betAmount = 100.0;
  }

  @Test
  void handle_WhenPlayerWins_ShouldUpdateRankingWithWin() {
    PlayDto playDto =
        new PlayDto(
            playId,
            PlayStatus.WON,
            userId,
            betAmount,
            new ArrayList<>(),
            new ArrayList<>(),
            18,
            16);

    PlayCompleted event = new PlayCompleted(playId, "play.completed", playDto);

    Ranking ranking = Ranking.create(userId);
    when(rankingRepository.findByUserId(userId)).thenReturn(Mono.just(ranking));
    when(rankingRepository.save(any(Ranking.class))).thenReturn(Mono.empty());

    subscriber.on(event);

    verify(rankingRepository).findByUserId(userId);
    verify(rankingRepository).save(rankingCaptor.capture());

    Ranking capturedRanking = rankingCaptor.getValue();
    assertEquals(1, capturedRanking.toPrimitives().wins());
    assertEquals(0, capturedRanking.toPrimitives().losses());
    assertEquals(betAmount, capturedRanking.toPrimitives().earnings());
  }

  @Test
  void handle_WhenPlayerLoses_ShouldUpdateRankingWithLoss() {
    PlayDto playDto =
        new PlayDto(
            playId,
            PlayStatus.LOST,
            userId,
            betAmount,
            new ArrayList<>(),
            new ArrayList<>(),
            16,
            18);

    PlayCompleted event = new PlayCompleted(playId, "play.completed", playDto);

    Ranking ranking = Ranking.create(userId);
    when(rankingRepository.findByUserId(userId)).thenReturn(Mono.just(ranking));
    when(rankingRepository.save(any(Ranking.class))).thenReturn(Mono.empty());

    subscriber.on(event);

    verify(rankingRepository).findByUserId(userId);
    verify(rankingRepository).save(rankingCaptor.capture());

    Ranking capturedRanking = rankingCaptor.getValue();
    assertEquals(0, capturedRanking.toPrimitives().wins());
    assertEquals(1, capturedRanking.toPrimitives().losses());
    assertEquals(-betAmount, capturedRanking.toPrimitives().earnings());
  }
}
