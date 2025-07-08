package dev.eduzavarce.blackjack_api.contexts.game.play.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of a blackjack play")
public enum PlayStatus {
  IN_PROGRESS,
  WON,
  LOST;

  public boolean isInProgress() {
    return this == IN_PROGRESS;
  }

  public boolean isWon() {
    return this == WON;
  }

  public boolean isLost() {
    return this == LOST;
  }
}
