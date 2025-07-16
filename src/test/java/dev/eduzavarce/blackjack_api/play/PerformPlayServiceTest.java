package dev.eduzavarce.blackjack_api.play;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import dev.eduzavarce.blackjack_api.contexts.game.play.application.PerformPlayService;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.Play;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayRepository;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayStatus;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.EventBus;

public class PerformPlayServiceTest {

  @Mock private PlayRepository playRepository;

  @Mock private EventBus eventBus;

  private PerformPlayService performPlayService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    performPlayService = new PerformPlayService(playRepository);
  }

  @Test
  public void testPerformPlay_WithValidPlay_ShouldDrawCardAndSaveToRepository() {
    String playId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    double betAmount = 100.0;
    Play play = Play.create(playId, userId, betAmount);

    when(playRepository.findPlayById(playId)).thenReturn(Mono.just(play));
    when(playRepository.save(any(Play.class))).thenReturn(Mono.empty());
    StepVerifier.create(performPlayService.execute(playId))
        .expectNextMatches(
            playDto ->
                playDto.id().equals(playId)
                    && playDto.userId().equals(userId)
                    && playDto.betAmount() == betAmount
                    && playDto.playerCards() != null
                    && playDto.dealerCards() != null)
        .verifyComplete();

    verify(playRepository, times(1)).findPlayById(playId);
    verify(playRepository, times(1)).save(any(Play.class));
  }

  @Test
  public void testPerformPlay_WithNonExistentPlay_ShouldThrowException() {
    String playId = UUID.randomUUID().toString();

    when(playRepository.findPlayById(playId)).thenReturn(Mono.empty());
    StepVerifier.create(performPlayService.execute(playId))
        .expectErrorMatches(
            throwable ->
                throwable instanceof IllegalArgumentException
                    && throwable.getMessage().equals("Play not found"))
        .verify();

    verify(playRepository, times(1)).findPlayById(playId);
    verify(playRepository, never()).save(any(Play.class));
  }

  @Test
  public void testPerformStand_WithValidPlay_ShouldStandAndSaveToRepository() {
    String playId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    double betAmount = 100.0;
    Play play = Play.create(playId, userId, betAmount);

    when(playRepository.findPlayById(playId)).thenReturn(Mono.just(play));
    when(playRepository.save(any(Play.class))).thenReturn(Mono.empty());

    StepVerifier.create(performPlayService.executeStand(playId))
        .expectNextMatches(
            playDto ->
                playDto.id().equals(playId)
                    && playDto.userId().equals(userId)
                    && playDto.betAmount() == betAmount
                    && playDto.playerCards() != null
                    && playDto.dealerCards() != null
                    && (playDto.status() == PlayStatus.WON || playDto.status() == PlayStatus.LOST))
        .verifyComplete();

    verify(playRepository, times(1)).findPlayById(playId);
    verify(playRepository, times(1)).save(any(Play.class));
  }

  @Test
  public void testPerformStand_WithNonExistentPlay_ShouldThrowException() {
    String playId = UUID.randomUUID().toString();

    when(playRepository.findPlayById(playId)).thenReturn(Mono.empty());
    StepVerifier.create(performPlayService.executeStand(playId))
        .expectErrorMatches(
            throwable ->
                throwable instanceof IllegalArgumentException
                    && throwable.getMessage().equals("Play not found"))
        .verify();

    verify(playRepository, times(1)).findPlayById(playId);
    verify(playRepository, never()).save(any(Play.class));
  }
}
