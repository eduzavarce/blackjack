package dev.eduzavarce.blackjack_api.contexts.accounts.infrastructure;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccount;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountDto;
import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountEntity;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;

@Table("user_accounts")
public class UserAccountMySqlEntity implements UserAccountEntity, Persistable<String> {
  @Transient private boolean isNew;
  @Id private String id;

  @Column("name")
  private String name;

  @Column("balance")
  private double balance;

  public UserAccountMySqlEntity() {}

  public UserAccountMySqlEntity(String id, String name, double balance, boolean isNew) {
    this.id = id;
    this.name = name;
    this.balance = balance;
    this.isNew = isNew;
  }

  public static UserAccountMySqlEntity fromDomain(UserAccount userAccount, boolean isNew) {
    UserAccountDto dto = userAccount.toPrimitives();
    return new UserAccountMySqlEntity(dto.id(), dto.name(), dto.balance(), isNew);
  }

  public static UserAccountMySqlEntity fromDomain(UserAccount userAccount) {
    UserAccountDto dto = userAccount.toPrimitives();
    return new UserAccountMySqlEntity(dto.id(), dto.name(), dto.balance(), false);
  }

  @Override
  public UserAccount toDomain() {
    UserDto userDto = new UserDto(id, name, "", balance);
    return UserAccount.create(userDto);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean isNew() {
    return isNew;
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
}
