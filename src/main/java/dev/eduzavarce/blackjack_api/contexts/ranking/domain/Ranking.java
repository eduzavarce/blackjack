package dev.eduzavarce.blackjack_api.contexts.ranking.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.AggregateRoot;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserId;

public class Ranking extends AggregateRoot {
  private final UserId userId;
  private int wins;
  private int losses;
  private double earnings;

  private Ranking(UserId userId, int wins, int losses, double earnings) {
    this.userId = userId;
    this.wins = wins;
    this.losses = losses;
    this.earnings = earnings;
  }

  public static Ranking create(String userId) {
    return new Ranking(new UserId(userId), 0, 0, 0);
  }

  public static Ranking fromPrimitives(String userId, int wins, int losses, double earnings) {
    return new Ranking(new UserId(userId), wins, losses, earnings);
  }

  public void addWin(double amount) {
    this.wins++;
    this.earnings += amount;
  }

  public void addLoss(double amount) {
    this.losses++;
    this.earnings -= amount;
  }

  public RankingDto toPrimitives() {
    return new RankingDto(userId.value(), wins, losses, earnings);
  }
}
