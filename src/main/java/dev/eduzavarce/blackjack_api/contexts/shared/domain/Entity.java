package dev.eduzavarce.blackjack_api.contexts.shared.domain;

public interface Entity<T> {
  T toDomain();
}
