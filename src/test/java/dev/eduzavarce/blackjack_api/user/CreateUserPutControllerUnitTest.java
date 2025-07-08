package dev.eduzavarce.blackjack_api.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import dev.eduzavarce.blackjack_api.contexts.auth.user.application.CreateUserService;
import dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure.CreateUserPutController;
import dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure.CreateUserRequest;

public class CreateUserPutControllerUnitTest {

  @Mock private CreateUserService createUserService;

  @InjectMocks private CreateUserPutController createUserPutController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateUser() {
    // Arrange
    String userId = UUID.randomUUID().toString();
    CreateUserRequest createUserRequest = new CreateUserRequest("Test User", "test@example.com");

    when(createUserService.execute(anyString(), anyString(), anyString())).thenReturn(Mono.empty());

    // Act & Assert
    StepVerifier.create(createUserPutController.createUser(userId, createUserRequest))
        .assertNext(
            response -> {
              assertEquals(HttpStatus.CREATED, response.getStatusCode());
              assertNull(response.getBody());
            })
        .verifyComplete();
  }
}
