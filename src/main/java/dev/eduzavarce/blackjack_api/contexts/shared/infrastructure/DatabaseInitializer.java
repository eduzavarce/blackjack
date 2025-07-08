package dev.eduzavarce.blackjack_api.contexts.shared.infrastructure;

import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class DatabaseInitializer {

  private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

  @Bean
  public ConnectionFactoryInitializer initializer(
      ConnectionFactory connectionFactory, @Value("classpath:schema.sql") Resource schemaScript) {

    logger.info("Initializing database schema from {}", schemaScript);

    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator(schemaScript);
    initializer.setDatabasePopulator(populator);

    // Ensure the initializer runs even if the database already exists
    initializer.setEnabled(true);

    logger.info("Database schema initialization configured successfully");

    return initializer;
  }
}
