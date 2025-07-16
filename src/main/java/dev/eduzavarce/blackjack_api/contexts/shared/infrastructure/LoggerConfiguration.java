package dev.eduzavarce.blackjack_api.contexts.shared.infrastructure;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfiguration {
    
    @Bean
    public Logger logger() {
        return new Slf4jLogger(LoggerConfiguration.class);
    }
}