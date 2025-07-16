package dev.eduzavarce.blackjack_api.contexts.game.play.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.Card;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.Play;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayDto;
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
    PlayDto dto = play.toPrimitives();

    // Convert player cards to maps
    List<Map<String, Object>> playerCardsMaps = new ArrayList<>();
    for (Card card : dto.playerCards()) {
      Map<String, Object> cardMap = new HashMap<>();
      cardMap.put("suit", card.suit());
      cardMap.put("value", card.value());
      if (card.rank() != null) {
        cardMap.put("rank", card.rank().name());
      }
      playerCardsMaps.add(cardMap);
    }

    // Convert dealer cards to maps
    List<Map<String, Object>> dealerCardsMaps = new ArrayList<>();
    for (Card card : dto.dealerCards()) {
      Map<String, Object> cardMap = new HashMap<>();
      cardMap.put("suit", card.suit());
      cardMap.put("value", card.value());
      if (card.rank() != null) {
        cardMap.put("rank", card.rank().name());
      }
      dealerCardsMaps.add(cardMap);
    }

    // Get the deck primitives from the play
    Map<String, Object> deckMap = play.getDeckPrimitives();

    return new PlayMongoEntity(
        dto.id(),
        dto.userId(),
        dto.betAmount(),
        dto.status().toString(),
        deckMap,
        playerCardsMaps,
        dealerCardsMaps);
  }

  public Play toDomain() {
    return Play.fromPrimitives(id, status, userId, betAmount, deck, playerCards, dealerCards);
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
