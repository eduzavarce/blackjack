package dev.eduzavarce.blackjack_api.contexts.shared.infrastructure;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.shared.domain.Logger;

@Component
public class RequestLoggingFilter implements WebFilter {

  private final Logger logger;

  public RequestLoggingFilter(Logger logger) {
    this.logger = logger;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    Instant start = Instant.now();
    String path = exchange.getRequest().getURI().getPath();
    String method = exchange.getRequest().getMethod().name();

    logger.info("Request started: " + method + " " + path);

    return chain
        .filter(exchange)
        .doFinally(
            signalType -> {
              Duration duration = Duration.between(start, Instant.now());
              int statusCode =
                  exchange.getResponse().getStatusCode() != null
                      ? exchange.getResponse().getStatusCode().value()
                      : 0;

              logger.info(
                  "Request completed: "
                      + method
                      + " "
                      + path
                      + " | Status: "
                      + statusCode
                      + " | Duration: "
                      + duration.toMillis()
                      + "ms");
            });
  }
}
