package dev.eduzavarce.blackjack_api.contexts.game.play.infrastructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.game.play.application.CreatePlayService;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.CreatePlayDto;
import dev.eduzavarce.blackjack_api.contexts.shared.infrastructure.ValidUUID;

@RestController
@Tag(name = "Game Play", description = "API for managing game plays")
public class CreatePlayPutController {
  private final CreatePlayService service;

  @Autowired
  public CreatePlayPutController(CreatePlayService service) {
    this.service = service;
  }

  @Operation(
      summary = "Create a new play",
      description = "Creates a new blackjack play with the specified ID, user ID, and bet amount")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Play created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body or validation error"),
        @ApiResponse(responseCode = "409", description = "Play already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @PutMapping("/api/v1/play/{id}")
  public Mono<ResponseEntity<Void>> execute(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Play creation details",
              required = true,
              content = @Content(schema = @Schema(implementation = CreatePlayRequest.class)))
          @Valid
          @RequestBody
          CreatePlayRequest request,
      @Parameter(description = "Play ID", required = true) @PathVariable @ValidUUID @NotBlank
          String id) {
    CreatePlayDto dto = new CreatePlayDto(id, request.userId(), request.betAmount());
    return service.execute(dto).then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
  }
}
