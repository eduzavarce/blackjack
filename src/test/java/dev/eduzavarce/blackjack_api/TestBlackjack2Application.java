package dev.eduzavarce.blackjack_api;

import org.springframework.boot.SpringApplication;

public class TestBlackjack2Application {

  public static void main(String[] args) {
    SpringApplication.from(Blackjack2Application::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
