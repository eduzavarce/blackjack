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

import dev.eduzavarce.blackjack_api.contexts.auth.user.application.ChangeUserNameService;
import dev.eduzavarce.blackjack_api.contexts.shared.infrastructure.ValidUUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "API for managing users")
public class ChangeUserNamePatchController {

  private final ChangeUserNameService changeUserNameService;

  public ChangeUserNamePatchController(ChangeUserNameService changeUserNameService) {
    this.changeUserNameService = changeUserNameService;
  }

  @Operation(summary = "Change user name", description = "Updates the name of an existing user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User name updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid user ID format or invalid request body"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @PatchMapping("/{id}/name")
  public Mono<ResponseEntity<Void>> changeUserName(
      @Parameter(description = "User ID", required = true) @PathVariable @ValidUUID String id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "New user name",
              required = true,
              content = @Content(schema = @Schema(implementation = ChangeUserNameRequest.class)))
          @RequestBody
          @Valid
          ChangeUserNameRequest request) {
    return changeUserNameService
        .execute(id, request.name())
        .then(Mono.just(ResponseEntity.status(HttpStatus.OK).build()));
  }
}
