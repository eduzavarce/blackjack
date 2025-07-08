package dev.eduzavarce.blackjack_api.contexts.accounts.domain;

public record UserAccountDto(String name, String id, double balance) {

  public UserAccountDto(String name, String id) {
    this(name, id, 0.0);
  }

  public UserAccountDto(String name, String id, double balance) {
    this.name = name;
    this.id = id;
    this.balance = balance;
  }
}
