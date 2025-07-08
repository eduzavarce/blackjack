package dev.eduzavarce.blackjack_api.contexts.game.user.domain;

import java.util.ArrayList;
import java.util.List;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.AggregateRoot;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserId;

public class GameUser extends AggregateRoot {
  private final GameUserName name;
  private final UserId id;
  private double balance;
  private final List<String> blackjackGames;

  private GameUser(UserId id, GameUserName name, double balance, List<String> blackjackGames) {
    this.id = id;
    this.name = name;
    this.balance = balance;
    this.blackjackGames = blackjackGames != null ? blackjackGames : new ArrayList<>();
  }

  public static GameUser create(GameUserCreate user) {
    final UserId userId = new UserId(user.id());
    final GameUserName userName = new GameUserName(user.name());
    final double userBalance = user.balance();
    final List<String> blackjackGames = user.blackjackGames();

    GameUser createdUser = new GameUser(userId, userName, userBalance, blackjackGames);
    createdUser.record(
        new GameUserRegistered(
            createdUser.id.value(), "game_user.registered", createdUser.toPrimitives()));

    return createdUser;
  }

  public void updateFunds(double amount) {
    this.balance += amount;
  }

  public void addBlackjackGame(String gameId) {
    if (!this.blackjackGames.contains(gameId)) {
      this.blackjackGames.add(gameId);
    }
  }

  public GameUserDto toPrimitives() {
    return new GameUserDto(id.value(), name.value(), balance, blackjackGames);
  }
}
