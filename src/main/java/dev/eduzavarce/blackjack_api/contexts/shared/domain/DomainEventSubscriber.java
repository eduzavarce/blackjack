package dev.eduzavarce.blackjack_api.contexts.shared.domain;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DomainEventSubscriber {
  Class<? extends DomainEvent>[] value();
}
