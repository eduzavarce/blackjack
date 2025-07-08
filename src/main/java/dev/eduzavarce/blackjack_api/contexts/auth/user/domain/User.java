package dev.eduzavarce.blackjack_api.contexts.auth.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.AggregateRoot;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserId;

public class User extends AggregateRoot {
  private final UserEmail email;
  private final UserId id;
  private final double balance;
  private UserName name;

  private User(UserId id, UserName name, UserEmail email, double balance) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.balance = balance;
  }

  public static User create(UserDto user) {

    final UserId userId = new UserId(user.id());
    final UserName userName = new UserName(user.name());
    final UserEmail userEmail = new UserEmail(user.email());
    final double userBalance = user.balance();
    User createdUser = new User(userId, userName, userEmail, userBalance);
    createdUser.record(
        new UserRegistered(createdUser.id.value(), "user.registered", createdUser.toPrimitives()));

    return createdUser;
  }

  private static User fromPrimitives(UserDto user) {

    return new User(
        new UserId(user.id()),
        new UserName(user.name()),
        new UserEmail(user.email()),
        user.balance());
  }

  public void changeName(String newName) {
    this.name = new UserName(newName);
    this.record(new UserNameChanged(this.id.value(), "user.name.changed", this.toPrimitives()));
  }

  public UserDto toPrimitives() {

    return new UserDto(id.value(), name.value(), email.value(), balance);
  }
}
