package dev.eduzavarce.blackjack_api.contexts.game.play.domain;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEvent;

public class PlayCreated extends DomainEvent {
  private final String eventName;
  private final PlayDto body;

  public PlayCreated(String aggregateId, String eventName, PlayDto primitives) {
    super(aggregateId, eventName, primitives);
    this.eventName = eventName;
    this.body = primitives;
  }

  @Override
  public String eventName() {
    return this.eventName;
  }

  @Override
  public DomainEvent fromPrimitives(
      String aggregateId, Record body, String eventId, String occurredOn) {
    if (!(body instanceof PlayDto playDto)) {
      throw new IllegalArgumentException("body is not a PlayDto");
    }
    return new PlayCreated(aggregateId, eventName, playDto);
  }
}
