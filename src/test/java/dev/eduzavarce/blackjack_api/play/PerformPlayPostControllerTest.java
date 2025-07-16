package dev.eduzavarce.blackjack_api.play;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.Card;
import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.Rank;
import dev.eduzavarce.blackjack_api.contexts.game.play.application.PerformPlayService;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayDto;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayStatus;
import dev.eduzavarce.blackjack_api.contexts.game.play.infrastructure.PerformPlayPostController;

@ExtendWith(MockitoExtension.class)
public class PerformPlayPostControllerTest {

  @Mock private PerformPlayService performPlayService;

  @InjectMocks private PerformPlayPostController controller;

  @Test
  public void testExecute_WithValidId_ShouldReturnOkResponse() {
    // Arrange
    String playId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    double betAmount = 100.0;

    List<Card> playerCards = new ArrayList<>();
    playerCards.add(new Card("Hearts", 10, Rank.TEN));
    playerCards.add(new Card("Spades", 8, Rank.EIGHT));

    List<Card> dealerCards = new ArrayList<>();
    dealerCards.add(new Card("Diamonds", 9, Rank.NINE));
    dealerCards.add(new Card("Clubs", 7, Rank.SEVEN));

    PlayDto playDto =
        new PlayDto(playId, PlayStatus.WON, userId, betAmount, playerCards, dealerCards, 18, 16);

    when(performPlayService.execute(playId)).thenReturn(Mono.just(playDto));

    // Act & Assert
    StepVerifier.create(controller.execute(playId))
        .expectNextMatches(
            response ->
                response.getStatusCode() == HttpStatus.OK
                    && response.getBody() != null
                    && response.getBody().id().equals(playId)
                    && response.getBody().status() == PlayStatus.WON
                    && response.getBody().userId().equals(userId)
                    && response.getBody().betAmount() == betAmount
                    && response.getBody().playerScore() == 18
                    && response.getBody().dealerScore() == 16)
        .verifyComplete();
  }

  @Test
  public void testExecute_WithNonExistentId_ShouldPropagateError() {
    // Arrange
    String playId = UUID.randomUUID().toString();
    when(performPlayService.execute(playId))
        .thenReturn(Mono.error(new IllegalArgumentException("Play not found")));

    // Act & Assert
    StepVerifier.create(controller.execute(playId))
        .expectErrorMatches(
            throwable ->
                throwable instanceof IllegalArgumentException
                    && throwable.getMessage().equals("Play not found"))
        .verify();
  }

  @Test
  public void testExecuteStand_WithValidId_ShouldReturnOkResponse() {
    // Arrange
    String playId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    double betAmount = 100.0;

    List<Card> playerCards = new ArrayList<>();
    playerCards.add(new Card("Hearts", 10, Rank.TEN));
    playerCards.add(new Card("Spades", 8, Rank.EIGHT));

    List<Card> dealerCards = new ArrayList<>();
    dealerCards.add(new Card("Diamonds", 9, Rank.NINE));
    dealerCards.add(new Card("Clubs", 7, Rank.SEVEN));
    dealerCards.add(new Card("Hearts", 5, Rank.FIVE)); // Dealer draws a card to reach 21

    PlayDto playDto =
        new PlayDto(playId, PlayStatus.LOST, userId, betAmount, playerCards, dealerCards, 18, 21);

    when(performPlayService.executeStand(playId)).thenReturn(Mono.just(playDto));

    // Act & Assert
    StepVerifier.create(controller.executeStand(playId))
        .expectNextMatches(
            response ->
                response.getStatusCode() == HttpStatus.OK
                    && response.getBody() != null
                    && response.getBody().id().equals(playId)
                    && response.getBody().status() == PlayStatus.LOST
                    && response.getBody().userId().equals(userId)
                    && response.getBody().betAmount() == betAmount
                    && response.getBody().playerScore() == 18
                    && response.getBody().dealerScore() == 21)
        .verifyComplete();
  }

  @Test
  public void testExecuteStand_WithNonExistentId_ShouldPropagateError() {
    // Arrange
    String playId = UUID.randomUUID().toString();
    when(performPlayService.executeStand(playId))
        .thenReturn(Mono.error(new IllegalArgumentException("Play not found")));

    // Act & Assert
    StepVerifier.create(controller.executeStand(playId))
        .expectErrorMatches(
            throwable ->
                throwable instanceof IllegalArgumentException
                    && throwable.getMessage().equals("Play not found"))
        .verify();
  }
}
