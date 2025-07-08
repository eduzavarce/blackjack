package dev.eduzavarce.blackjack_api.user;

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

import dev.eduzavarce.blackjack_api.Blackjack2Application;
import dev.eduzavarce.blackjack_api.TestcontainersConfiguration;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountRepository;
import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserRepository;
import dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure.CreateUserRequest;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserRepository;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(
    classes = Blackjack2Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CreateUserPutControllerIntegrationTest {

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
  void testCreateUser() {
    // Store userId in class field for cleanup
    this.userId = UUID.randomUUID().toString();

    CreateUserRequest createUserRequest = new CreateUserRequest("Test User", "test@example.com");

    webTestClient
        .put()
        .uri("/api/v1/users/{id}", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(createUserRequest))
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .isEmpty();

    // Wait a bit for async operations to complete
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
