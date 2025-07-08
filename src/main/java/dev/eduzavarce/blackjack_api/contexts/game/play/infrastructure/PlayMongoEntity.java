package dev.eduzavarce.blackjack_api.contexts.game.play.infrastructure;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.eduzavarce.blackjack_api.contexts.game.play.domain.Play;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayEntity;

@Document(collection = "plays")
public class PlayMongoEntity implements PlayEntity {
  @Id private String id;
  private String userId;
  private double betAmount;
  private String status;
  private Map<String, Object> deck;
  private List<Map<String, Object>> playerCards;
  private List<Map<String, Object>> dealerCards;

  public PlayMongoEntity() {}

  public PlayMongoEntity(
      String id,
      String userId,
      double betAmount,
      String status,
      Map<String, Object> deck,
      List<Map<String, Object>> playerCards,
      List<Map<String, Object>> dealerCards) {
    this.id = id;
    this.userId = userId;
    this.betAmount = betAmount;
    this.status = status;
    this.deck = deck;
    this.playerCards = playerCards;
    this.dealerCards = dealerCards;
  }

  public static PlayMongoEntity fromDomain(Play play) {
    return new PlayMongoEntity();
  }

  public Play toDomain() {
    return Play.create(id, userId, betAmount);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public double getBetAmount() {
    return betAmount;
  }

  public void setBetAmount(double betAmount) {
    this.betAmount = betAmount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Map<String, Object> getDeck() {
    return deck;
  }

  public void setDeck(Map<String, Object> deck) {
    this.deck = deck;
  }

  public List<Map<String, Object>> getPlayerCards() {
    return playerCards;
  }

  public void setPlayerCards(List<Map<String, Object>> playerCards) {
    this.playerCards = playerCards;
  }

  public List<Map<String, Object>> getDealerCards() {
    return dealerCards;
  }

  public void setDealerCards(List<Map<String, Object>> dealerCards) {
    this.dealerCards = dealerCards;
  }
}
