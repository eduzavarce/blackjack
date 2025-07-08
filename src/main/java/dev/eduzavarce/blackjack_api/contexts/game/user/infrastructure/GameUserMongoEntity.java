package dev.eduzavarce.blackjack_api.contexts.game.user.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUser;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserCreate;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserDto;
import dev.eduzavarce.blackjack_api.contexts.game.user.domain.GameUserEntity;

@Document(collection = "gameUsers")
public class GameUserMongoEntity implements GameUserEntity {
  @Id private String id;
  private String name;
  private double balance;
  private List<String> blackjackGames;

  public GameUserMongoEntity() {
    this.blackjackGames = new ArrayList<>();
  }

  public GameUserMongoEntity(String id, String name, double balance, List<String> blackjackGames) {
    this.id = id;
    this.name = name;
    this.balance = balance;
    this.blackjackGames = blackjackGames != null ? blackjackGames : new ArrayList<>();
  }

  public static GameUserMongoEntity fromDomain(GameUser gameUser) {
    GameUserDto dto = gameUser.toPrimitives();
    return new GameUserMongoEntity(dto.id(), dto.name(), dto.balance(), dto.blackjackGames());
  }

  public GameUser toDomain() {
    return GameUser.create(new GameUserCreate(id, name, balance, blackjackGames));
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public List<String> getBlackjackGames() {
    return blackjackGames;
  }

  public void setBlackjackGames(List<String> blackjackGames) {
    this.blackjackGames = blackjackGames;
  }

  public void addBlackjackGame(String gameId) {
    if (!this.blackjackGames.contains(gameId)) {
      this.blackjackGames.add(gameId);
    }
  }
}
