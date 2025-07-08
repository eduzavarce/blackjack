package dev.eduzavarce.blackjack_api.contexts.game.user.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEvent;

public class GameUserRegistered extends DomainEvent {
  private final String eventName;
  private final GameUserDto body;

  public GameUserRegistered(String aggregateId, String eventName, GameUserDto body) {
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
    if (!(body instanceof GameUserDto gameUserDto)) {
      throw new IllegalArgumentException("body is not a GameUserDto");
    }
    return new GameUserRegistered(aggregateId, eventName, gameUserDto);
  }
}
