package dev.eduzavarce.blackjack_api.contexts.accounts.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEvent;

public class UserAccountCreated extends DomainEvent {
  private final String eventName;
  private final UserAccountDto body;

  public UserAccountCreated(String aggregateId, String eventName, UserAccountDto body) {
    super(aggregateId, eventName, body);
    this.eventName = eventName;
    this.body = body;
  }

  @Override
  public String eventName() {
    return this.eventName;
  }

  @Override
  public DomainEvent fromPrimitives(
      String aggregateId, Record body, String eventId, String occurredOn) {
    if (!(body instanceof UserAccountDto userAccountDto)) {
      throw new IllegalArgumentException("body is not a UserAccountDto");
    }
    return new UserAccountCreated(aggregateId, eventName, userAccountDto);
  }
}
