package dev.eduzavarce.blackjack_api.ranking;

import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import dev.eduzavarce.blackjack_api.contexts.ranking.application.GetScoreboardService;
import dev.eduzavarce.blackjack_api.contexts.ranking.domain.RankingDto;
import dev.eduzavarce.blackjack_api.contexts.ranking.infrastructure.GetScoreboardGetController;

@ExtendWith(MockitoExtension.class)
public class GetScoreboardGetControllerTest {

  @Mock private GetScoreboardService getScoreboardService;

  @InjectMocks private GetScoreboardGetController controller;

  @Test
  public void testExecute_WithRankings_ShouldReturnOkResponse() {
    // Arrange
    int limit = 10;
    String userId1 = UUID.randomUUID().toString();
    String userId2 = UUID.randomUUID().toString();

    RankingDto ranking1 = new RankingDto(userId1, 5, 2, 300.0);
    RankingDto ranking2 = new RankingDto(userId2, 3, 4, 150.0);

    when(getScoreboardService.execute(limit)).thenReturn(Flux.just(ranking1, ranking2));

    // Act & Assert
    StepVerifier.create(controller.execute(limit))
        .expectNextMatches(
            response ->
                response.getStatusCode() == HttpStatus.OK
                    && response.getBody() != null
                    && response.getBody().size() == 2
                    && response.getBody().get(0).userId().equals(userId1)
                    && response.getBody().get(0).wins() == 5
                    && response.getBody().get(0).losses() == 2
                    && response.getBody().get(0).earnings() == 300.0
                    && response.getBody().get(1).userId().equals(userId2)
                    && response.getBody().get(1).wins() == 3
                    && response.getBody().get(1).losses() == 4
                    && response.getBody().get(1).earnings() == 150.0)
        .verifyComplete();
  }

  @Test
  public void testExecute_WithNoRankings_ShouldReturnEmptyList() {
    // Arrange
    int limit = 10;
    when(getScoreboardService.execute(limit)).thenReturn(Flux.empty());

    // Act & Assert
    StepVerifier.create(controller.execute(limit))
        .expectNextMatches(
            response ->
                response.getStatusCode() == HttpStatus.OK
                    && response.getBody() != null
                    && response.getBody().isEmpty())
        .verifyComplete();
  }
}
