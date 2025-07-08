package dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.auth.user.application.CreateUserService;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "API for managing users")
public class CreateUserPutController {

  private final CreateUserService createUserService;

  public CreateUserPutController(CreateUserService createUserService) {
    this.createUserService = createUserService;
  }

  @Operation(
      summary = "Create a new user",
      description = "Creates a new user with the specified ID, name, and email")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body or validation error"),
        @ApiResponse(responseCode = "409", description = "User already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @PutMapping("/{id}")
  public Mono<ResponseEntity<Void>> createUser(
      @Parameter(description = "User ID", required = true) @PathVariable String id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "User details",
              required = true,
              content = @Content(schema = @Schema(implementation = CreateUserRequest.class)))
          @RequestBody
          @Valid
          CreateUserRequest createUserRequest) {
    return createUserService
        .execute(id, createUserRequest.name(), createUserRequest.email())
        .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
  }
}
