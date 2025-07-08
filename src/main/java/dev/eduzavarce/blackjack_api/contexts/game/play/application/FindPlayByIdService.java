package dev.eduzavarce.blackjack_api.contexts.game.play.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayDto;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayRepository;

@Service
public class FindPlayByIdService {
  private final PlayRepository repository;

  @Autowired
  public FindPlayByIdService(PlayRepository repository) {
    this.repository = repository;
  }

  public Mono<PlayDto> execute(String id) {
    return repository.findPlayById(id).map(play -> play.toPrimitives());
  }
}
