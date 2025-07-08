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

import dev.eduzavarce.blackjack_api.contexts.auth.user.application.ChangeUserNameService;
import dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure.ChangeUserNamePatchController;
import dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure.ChangeUserNameRequest;

public class ChangeUserNamePatchControllerUnitTest {

  @Mock private ChangeUserNameService changeUserNameService;

  @InjectMocks private ChangeUserNamePatchController changeUserNamePatchController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testChangeUserName() {
    // Arrange
    String userId = UUID.randomUUID().toString();
    ChangeUserNameRequest changeUserNameRequest = new ChangeUserNameRequest("New User Name");

    when(changeUserNameService.execute(anyString(), anyString())).thenReturn(Mono.empty());

    // Act & Assert
    StepVerifier.create(changeUserNamePatchController.changeUserName(userId, changeUserNameRequest))
        .assertNext(
            response -> {
              assertEquals(HttpStatus.OK, response.getStatusCode());
              assertNull(response.getBody());
            })
        .verifyComplete();
  }
}
