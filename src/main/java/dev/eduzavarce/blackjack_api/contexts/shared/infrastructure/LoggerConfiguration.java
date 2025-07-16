package dev.eduzavarce.blackjack_api.contexts.shared.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.Logger;

@Configuration
public class LoggerConfiguration {

  @Bean
  public Logger logger() {
    return new Slf4jLogger(LoggerConfiguration.class);
  }
}
