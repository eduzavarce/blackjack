package dev.eduzavarce.blackjack_api.contexts.ranking.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Player ranking information")
public record RankingDto(
    @Schema(description = "ID of the user", example = "123e4567-e89b-12d3-a456-426614174000")
        String userId,
    @Schema(description = "Number of games won", example = "15") int wins,
    @Schema(description = "Number of games lost", example = "5") int losses,
    @Schema(description = "Total earnings from all games", example = "1250.50") double earnings) {
  // Calculate win rate as a percentage
  @Schema(description = "Win rate as a percentage", example = "75.0")
  public double winRate() {
    int totalGames = wins + losses;
    return totalGames > 0 ? (double) wins / totalGames * 100 : 0;
  }
}
