package dev.eduzavarce.blackjack_api.contexts.shared.infrastructure;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.DomainEventSubscriber;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.EventBus;

@Configuration
public class EventBusConfiguration {

  private final ApplicationContext applicationContext;

  public EventBusConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {
    EventBus eventBus = applicationContext.getBean(EventBus.class);

    if (eventBus instanceof InMemoryEventBus) {
      Map<String, Object> subscribers =
          applicationContext.getBeansWithAnnotation(DomainEventSubscriber.class);

      for (Object subscriber : subscribers.values()) {
        ((InMemoryEventBus) eventBus).register(subscriber);
      }
    }
  }
}
