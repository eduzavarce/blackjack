package dev.eduzavarce.blackjack_api.contexts.accounts.domain;

import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserName;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.AggregateRoot;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserId;

public class UserAccount extends AggregateRoot {
  private final UserName name;
  private final UserId id;
  private double balance;

  private UserAccount(UserDto userDto) {
    this.id = new UserId(userDto.id());
    this.name = new UserName(userDto.name());
    this.balance = userDto.balance();
  }

  private UserAccount(UserAccountDto dto) {
    this.id = new UserId(dto.id());
    this.name = new UserName(dto.name());
    this.balance = dto.balance();
  }

  public static UserAccount create(UserDto userDto) {
    UserAccount createdUser = new UserAccount(userDto);
    createdUser.record(
        new UserAccountCreated(
            createdUser.id.value(), "user.account.created", createdUser.toPrimitives()));
    return createdUser;
  }

  public void increaseFunds(double amount) {
    this.balance += amount;
  }

  public UserAccountDto toPrimitives() {
    return new UserAccountDto(name.value(), id.value(), balance);
  }

  public UserAccount fromPrimitives(UserAccountDto userDto) {
    return new UserAccount(userDto);
  }

  public void decreaseFunds(double v) {
    this.balance -= v;
  }
}
