package dev.eduzavarce.blackjack_api.contexts.shared.infrastructure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEvent;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEventSubscriber;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.EventBus;

@Service
@Primary
public class InMemoryEventBus implements EventBus {
  private final Map<Class<? extends DomainEvent>, List<Object>> subscribers = new HashMap<>();

  public void register(Object subscriber) {
    Class<?> subscriberClass = subscriber.getClass();
    DomainEventSubscriber annotation = subscriberClass.getAnnotation(DomainEventSubscriber.class);

    if (annotation != null) {
      Class<? extends DomainEvent>[] eventClasses = annotation.value();
      for (Class<? extends DomainEvent> eventClass : eventClasses) {
        subscribers.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(subscriber);
      }
    }
  }

  @Override
  public void publish(List<DomainEvent> events) {
    events.forEach(this::publish);
  }

  private void publish(DomainEvent event) {
    if (event == null) {
      throw new IllegalArgumentException("Event cannot be null");
    }

    Class<? extends DomainEvent> eventClass = event.getClass();
    List<Object> eventSubscribers = subscribers.getOrDefault(eventClass, new ArrayList<>());

    for (Object subscriber : eventSubscribers) {
      try {
        Method onMethod = subscriber.getClass().getMethod("on", eventClass);
        onMethod.invoke(subscriber, event);
      } catch (Exception e) {
        throw new RuntimeException("Error publishing event: " + e.getMessage(), e);
      }
    }
  }
}
