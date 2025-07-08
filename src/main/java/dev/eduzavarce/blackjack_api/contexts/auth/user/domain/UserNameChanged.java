package dev.eduzavarce.blackjack_api.contexts.auth.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEvent;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserDto;

public class UserNameChanged extends DomainEvent {
  private final String eventName;
  private final UserDto body;

  public UserNameChanged(String aggregateId, String eventName, UserDto body) {
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
    if (!(body instanceof UserDto userDto)) {
      throw new IllegalArgumentException("body is not a UserDto");
    }
    return new UserNameChanged(aggregateId, eventName, userDto);
  }
}
