package dev.eduzavarce.blackjack_api.contexts.game.play.infrastructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.game.play.application.PerformPlayService;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayDto;
import dev.eduzavarce.blackjack_api.contexts.shared.infrastructure.ValidUUID;

@RestController
@Tag(name = "Game Play", description = "API for managing game plays")
public class PerformPlayPostController {
  private final PerformPlayService service;

  @Autowired
  public PerformPlayPostController(PerformPlayService service) {
    this.service = service;
  }

  @Operation(
      summary = "Perform a play",
      description = "Executes a blackjack play (draws cards) for the specified play ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Play performed successfully",
            content = @Content(schema = @Schema(implementation = PlayDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid play ID format"),
        @ApiResponse(responseCode = "404", description = "Play not found"),
        @ApiResponse(responseCode = "409", description = "Play is already completed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @PostMapping("/api/v1/play/{id}")
  public Mono<ResponseEntity<PlayDto>> execute(
      @Parameter(description = "Play ID", required = true) @PathVariable @ValidUUID @NotBlank
          String id) {
    return service.execute(id).map(ResponseEntity::ok);
  }
}
