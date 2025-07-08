package dev.eduzavarce.blackjack_api.contexts.game.deck.infrastructure;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure.CardDocument;
import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.Deck;
import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.DeckEntity;

@Document(collection = "decks")
public class DeckMongoEntity implements DeckEntity {
  @Id private String id;
  private List<CardDocument> cards;

  public DeckMongoEntity() {}

  public DeckMongoEntity(String id, List<CardDocument> cards) {
    this.id = id;
    this.cards = cards;
  }

  public static DeckMongoEntity fromDomain(Deck deck) {
    Map<String, Object> primitives = deck.toPrimitives();
    String id = (String) primitives.get("id");
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> cardsList = (List<Map<String, Object>>) primitives.get("cards");

    List<CardDocument> cardDocuments =
        cardsList.stream()
            .map(
                cardMap ->
                    new CardDocument(
                        (String) cardMap.get("suit"), ((Number) cardMap.get("value")).intValue()))
            .toList();

    return new DeckMongoEntity(id, cardDocuments);
  }

  @Override
  public Deck toDomain() {
    Map<String, Object> primitives =
        Map.of(
            "id",
            id,
            "cards",
            cards.stream()
                .map(
                    card ->
                        Map.of(
                            "suit", card.getSuit(),
                            "value", card.getValue()))
                .toList());
    return Deck.fromPrimitives(primitives);
  }
}
