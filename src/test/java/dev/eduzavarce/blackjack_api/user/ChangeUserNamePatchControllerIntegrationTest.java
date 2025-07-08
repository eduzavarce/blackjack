package dev.eduzavarce.blackjack_api.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;

import dev.eduzavarce.blackjack_api.Blackjack2Application;
import dev.eduzavarce.blackjack_api.TestcontainersConfiguration;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccount;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountDto;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountRepository;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserRepository;
import dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure.ChangeUserNameRequest;
import dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure.CreateUserRequest;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUser;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserDto;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserRepository;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(
    classes = Blackjack2Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ChangeUserNamePatchControllerIntegrationTest {

  @Autowired private WebTestClient webTestClient;
  @Autowired private UserRepository userRepository;
  @Autowired private GameUserRepository gameUserRepository;
  @Autowired private UserAccountRepository userAccountRepository;

  private String userId;

  @AfterEach
  void cleanup() {
    if (userId != null) {
      // Delete data from all repositories
      userRepository.deleteById(userId).block();
      gameUserRepository.deleteById(userId).block();
      userAccountRepository.deleteById(userId).block();
    }
  }

  @Test
  void testChangeUserName() {
    // Store userId in class field for cleanup
    this.userId = UUID.randomUUID().toString();
    String initialName = "Initial User Name";
    String newName = "New User Name";
    CreateUserRequest createUserRequest = new CreateUserRequest(initialName, "test@example.com");
    webTestClient
        .put()
        .uri("/api/v1/users/{id}", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(createUserRequest))
        .exchange()
        .expectStatus()
        .isCreated();

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    ChangeUserNameRequest changeUserNameRequest = new ChangeUserNameRequest(newName);
    webTestClient
        .patch()
        .uri("/api/v1/users/{id}/name", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(changeUserNameRequest))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    gameUserRepository
        .findById(userId)
        .as(StepVerifier::create)
        .assertNext(
            gameUserEntity -> {
              GameUser gameUser = gameUserEntity.toDomain();
              GameUserDto gameUserDto = gameUser.toPrimitives();
              assertEquals(newName, gameUserDto.name());
            })
        .verifyComplete();
    userAccountRepository
        .findById(userId)
        .as(StepVerifier::create)
        .assertNext(
            userAccountEntity -> {
              UserAccount userAccount = userAccountEntity.toDomain();
              UserAccountDto userAccountDto = userAccount.toPrimitives();
              assertEquals(newName, userAccountDto.name());
            })
        .verifyComplete();
  }
}
