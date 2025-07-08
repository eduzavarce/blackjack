package dev.eduzavarce.blackjack_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = {"dev.eduzavarce.blackjack_api.contexts"})
public class Blackjack2Application {

  public static void main(String[] args) {
    SpringApplication.run(Blackjack2Application.class, args);
  }
}
