package dev.eduzavarce.blackjack_api.contexts.shared.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot {
  private List<DomainEvent> domainEvents = new ArrayList<>();

  public final List<DomainEvent> pullDomainEvents() {
    List<DomainEvent> events = domainEvents;

    domainEvents = Collections.emptyList();

    return events;
  }

  protected final void record(DomainEvent event) {
    domainEvents.add(event);
  }
}
