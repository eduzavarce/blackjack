package dev.eduzavarce.blackjack_api.contexts.ranking.infrastructure;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.eduzavarce.blackjack_api.contexts.ranking.domain.Ranking;

@Document(collection = "rankings")
public class RankingMongoEntity {
  @Id private String userId;
  private int wins;
  private int losses;
  private double earnings;

  public RankingMongoEntity() {
    // Required by MongoDB
  }

  public RankingMongoEntity(String userId, int wins, int losses, double earnings) {
    this.userId = userId;
    this.wins = wins;
    this.losses = losses;
    this.earnings = earnings;
  }

  public static RankingMongoEntity fromDomain(Ranking ranking) {
    var primitives = ranking.toPrimitives();
    return new RankingMongoEntity(
        primitives.userId(), primitives.wins(), primitives.losses(), primitives.earnings());
  }

  public Ranking toDomain() {
    return Ranking.fromPrimitives(userId, wins, losses, earnings);
  }

  // Getters and setters
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public int getWins() {
    return wins;
  }

  public void setWins(int wins) {
    this.wins = wins;
  }

  public int getLosses() {
    return losses;
  }

  public void setLosses(int losses) {
    this.losses = losses;
  }

  public double getEarnings() {
    return earnings;
  }

  public void setEarnings(double earnings) {
    this.earnings = earnings;
  }
}
