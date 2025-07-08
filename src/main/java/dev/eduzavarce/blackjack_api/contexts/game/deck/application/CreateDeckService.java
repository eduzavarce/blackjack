package dev.eduzavarce.blackjack_api.contexts.game.deck.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.Deck;
import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.DeckRepository;
import dev.eduzavarce.blackjack_api.contexts.game.deck.infrastructure.DeckMongoEntity;
import dev.eduzavarce.blackjack_api.contexts.game.play.domain.PlayId;

@Service
public class CreateDeckService {
  private final DeckRepository deckRepository;

  @Autowired
  public CreateDeckService(DeckRepository deckRepository) {
    this.deckRepository = deckRepository;
  }

  public void create(PlayId id) {
    Deck deck = Deck.create(id.value());
    DeckMongoEntity deckMongoEntity = DeckMongoEntity.fromDomain(deck);
    deckRepository.save(deckMongoEntity);
  }
}
