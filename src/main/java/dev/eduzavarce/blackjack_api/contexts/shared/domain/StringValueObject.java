package dev.eduzavarce.blackjack_api.contexts.shared.domain;

import java.util.Objects;

public abstract class StringValueObject {
  private final String value;

  protected StringValueObject(String value) {
    this.value = value.trim();
  }

  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return this.value();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StringValueObject that)) {
      return false;
    }
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
