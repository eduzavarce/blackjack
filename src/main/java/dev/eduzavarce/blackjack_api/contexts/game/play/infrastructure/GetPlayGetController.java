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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.game.play.application.FindPlayByIdService;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayDto;
import dev.eduzavarce.blackjack_api.contexts.shared.infrastructure.ValidUUID;

@RestController
@Tag(name = "Game Play", description = "API for managing game plays")
public class GetPlayGetController {
  private final FindPlayByIdService service;

  @Autowired
  public GetPlayGetController(FindPlayByIdService service) {
    this.service = service;
  }

  @Operation(summary = "Get play by ID", description = "Retrieves a play by its unique identifier")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Play found",
            content = @Content(schema = @Schema(implementation = PlayDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid play ID format"),
        @ApiResponse(responseCode = "404", description = "Play not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @GetMapping("/api/v1/play/{id}")
  public Mono<ResponseEntity<PlayDto>> execute(
      @Parameter(description = "Play ID", required = true) @PathVariable @ValidUUID @NotBlank
          String id) {
    return service
        .execute(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
