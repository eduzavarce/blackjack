package dev.eduzavarce.blackjack_api.contexts.ranking.infrastructure;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.ranking.application.GetScoreboardService;
import dev.eduzavarce.blackjack_api.contexts.ranking.domain.RankingDto;

@RestController
@Tag(name = "Ranking", description = "API for retrieving player rankings")
public class GetScoreboardGetController {
  private final GetScoreboardService service;

  @Autowired
  public GetScoreboardGetController(GetScoreboardService service) {
    this.service = service;
  }

  @Operation(
      summary = "Get player rankings",
      description = "Retrieves a list of player rankings sorted by earnings")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Rankings retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RankingDto.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @GetMapping("/api/v1/ranking")
  public Mono<ResponseEntity<List<RankingDto>>> execute(
      @Parameter(description = "Maximum number of rankings to return", example = "10")
          @RequestParam(required = false, defaultValue = "10")
          int limit) {
    return service
        .execute(limit)
        .collectList()
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.ok(List.of()));
  }
}
