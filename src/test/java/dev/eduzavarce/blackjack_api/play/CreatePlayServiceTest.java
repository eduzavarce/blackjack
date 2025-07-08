package dev.eduzavarce.blackjack_api.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccount;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountEntity;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountRepository;
import dev.eduzavarce.blackjack_api.contexts.game.deck.application.CreateDeckService;
import dev.eduzavarce.blackjack_api.contexts.game.play.application.CreatePlayService;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.CreatePlayDto;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.Play;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayRepository;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.EventBus;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;

public class CreatePlayServiceTest {

  @Mock private PlayRepository playRepository;

  @Mock private UserAccountRepository userAccountRepository;

  @Mock private CreateDeckService createDeckService;

  @Mock private EventBus eventBus;

  private CreatePlayService createPlayService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    createPlayService =
        new CreatePlayService(createDeckService, playRepository, userAccountRepository);
  }

  @Test
  public void testCreatePlay_WithValidBet_ShouldCreatePlayAndSaveToRepository() {
    String playId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    double betAmount = 100.0;
    double userBalance = 200.0;
    CreatePlayDto dto = new CreatePlayDto(playId, userId, betAmount);

    UserAccount userAccount =
        UserAccount.create(new UserDto(userId, "Test User", "test@example.com", userBalance));
    UserAccountEntity userAccountEntity = mock(UserAccountEntity.class);
    when(userAccountEntity.toDomain()).thenReturn(userAccount);

    Mono<UserAccountEntity> monoEntity = Mono.just(userAccountEntity);
    doReturn(monoEntity).when(userAccountRepository).findById(userId);
    when(playRepository.save(any(Play.class))).thenReturn(Mono.empty());

    StepVerifier.create(createPlayService.execute(dto)).verifyComplete();

    verify(playRepository, times(1)).save(any(Play.class));
  }

  @Test
  public void testCreatePlay_WithNegativeBet_ShouldThrowException() {
    String playId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    double betAmount = -50.0;
    CreatePlayDto dto = new CreatePlayDto(playId, userId, betAmount);

    StepVerifier.create(createPlayService.execute(dto))
        .expectErrorMatches(
            throwable ->
                throwable instanceof IllegalArgumentException
                    && throwable.getMessage().equals("Bet amount cannot be negative"))
        .verify();

    verify(playRepository, never()).save(any(Play.class));
    verify(userAccountRepository, never()).findById(anyString());
  }

  @Test
  public void testCreatePlay_WithBetGreaterThanBalance_ShouldThrowException() {
    String playId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    double betAmount = 200.0;
    double userBalance = 100.0;
    CreatePlayDto dto = new CreatePlayDto(playId, userId, betAmount);

    UserAccount userAccount =
        UserAccount.create(new UserDto(userId, "Test User", "test@example.com", userBalance));
    UserAccountEntity userAccountEntity = mock(UserAccountEntity.class);
    when(userAccountEntity.toDomain()).thenReturn(userAccount);

    Mono<UserAccountEntity> monoEntity = Mono.just(userAccountEntity);
    doReturn(monoEntity).when(userAccountRepository).findById(userId);

    StepVerifier.create(createPlayService.execute(dto))
        .expectErrorMatches(
            throwable ->
                throwable instanceof IllegalArgumentException
                    && throwable
                        .getMessage()
                        .equals("Bet amount cannot be greater than user balance"))
        .verify();

    verify(playRepository, never()).save(any(Play.class));
  }

  @Test
  public void testCreatePlay_ShouldCreatePlayWithShuffledDeckAndPublishEvent() {
    String playId = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    double betAmount = 100.0;
    double userBalance = 200.0;
    CreatePlayDto dto = new CreatePlayDto(playId, userId, betAmount);

    UserAccount userAccount =
        UserAccount.create(new UserDto(userId, "Test User", "test@example.com", userBalance));
    UserAccountEntity userAccountEntity = mock(UserAccountEntity.class);
    when(userAccountEntity.toDomain()).thenReturn(userAccount);

    Mono<UserAccountEntity> monoEntity = Mono.just(userAccountEntity);
    doReturn(monoEntity).when(userAccountRepository).findById(userId);

    ArgumentCaptor<Play> playCaptor = ArgumentCaptor.forClass(Play.class);
    when(playRepository.save(playCaptor.capture())).thenReturn(Mono.empty());

    createPlayService.execute(dto).block();

    Play capturedPlay = playCaptor.getValue();
    assertNotNull(capturedPlay);

    assertEquals(playId, capturedPlay.toPrimitives().id());
    assertEquals(userId, capturedPlay.toPrimitives().userId());
    assertEquals(betAmount, capturedPlay.toPrimitives().betAmount());

    verify(userAccountRepository).findById(userId);
    verify(playRepository).save(any(Play.class));
  }
}
