package dev.eduzavarce.blackjack_api.contexts.shared.infrastructure;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class DatabaseInitializer {


    @Bean
    public ConnectionFactoryInitializer initializer(
            ConnectionFactory connectionFactory, @Value("classpath:schema.sql") Resource schemaScript) {


        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(schemaScript);
        initializer.setDatabasePopulator(populator);

        // Ensure the initializer runs even if the database already exists
        initializer.setEnabled(true);


        return initializer;
    }
}
