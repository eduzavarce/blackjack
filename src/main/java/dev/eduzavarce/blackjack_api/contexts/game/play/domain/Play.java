package dev.eduzavarce.blackjack_api.contexts.game.play.domain;

import java.util.ArrayList;
import java.util.List;

import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.Card;
import dev.eduzavarce.blackjack_api.contexts.game.deck.domain.Deck;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.AggregateRoot;
import dev.eduzavarce.blackjack_api.contexts.shared.domain.UserId;

public class Play extends AggregateRoot {
  private final PlayId id;
  private PlayStatus status;
  private final UserId userId;
  private final double betAmount;
  private final Deck deck;
  private final List<Card> playerCards;
  private final List<Card> dealerCards;
  private static final int BLACKJACK_VALUE = 21;
  private static final int DEALER_STAND_VALUE = 17;

  private Play(PlayId id, PlayStatus status, UserId userId, double betAmount) {
    this.id = id;
    this.status = status;
    this.userId = userId;
    this.betAmount = betAmount;
    this.deck = Deck.create(id.value());
    this.playerCards = new ArrayList<>();
    this.dealerCards = new ArrayList<>();

    this.playerCards.add(deck.drawCard());
    this.dealerCards.add(deck.drawCard());
    this.playerCards.add(deck.drawCard());
    this.dealerCards.add(deck.drawCard());
  }

  public static Play create(String id, String userId, double betAmount) {
    Play play = new Play(new PlayId(id), PlayStatus.IN_PROGRESS, new UserId(userId), betAmount);
    play.record(new PlayCreated(play.id.value(), "play.created", play.toPrimitives()));

    return play;
  }

  public Play performPlay() {
    if (!status.isInProgress()) {
      throw new IllegalStateException("Cannot perform play on a game that is not in progress");
    }

    playerCards.add(deck.drawCard());

    int playerScore = calculateHandValue(playerCards);
    if (playerScore > BLACKJACK_VALUE) {
      this.status = PlayStatus.LOST;
      record(new PlayCompleted(id.value(), "play.completed", toPrimitives()));
      return this;
    }

    return this;
  }

  public Play performDealerPlay() {
    if (!status.isInProgress()) {
      throw new IllegalStateException(
          "Cannot perform dealer play on a game that is not in progress");
    }

    int dealerScore = calculateHandValue(dealerCards);
    while (dealerScore < DEALER_STAND_VALUE) {
      dealerCards.add(deck.drawCard());
      dealerScore = calculateHandValue(dealerCards);
    }

    determineWinner();

    record(new PlayCompleted(id.value(), "play.completed", toPrimitives()));

    return this;
  }

  private void determineWinner() {
    int playerScore = calculateHandValue(playerCards);
    int dealerScore = calculateHandValue(dealerCards);

    if (playerScore > BLACKJACK_VALUE) {
      this.status = PlayStatus.LOST;
      return;
    }

    if (dealerScore > BLACKJACK_VALUE) {
      this.status = PlayStatus.WON;
      return;
    }

    if (playerScore > dealerScore) {
      this.status = PlayStatus.WON;
    } else if (playerScore < dealerScore) {
      this.status = PlayStatus.LOST;
    } else {
      this.status = PlayStatus.WON;
    }
  }

  public int calculateHandValue(List<Card> cards) {
    int value = 0;
    int aceCount = 0;

    for (Card card : cards) {
      if (card.value() == 1) {
        aceCount++;
      }
      value += card.value();
    }

    for (int i = 0; i < aceCount; i++) {
      if (value + 10 <= BLACKJACK_VALUE) {
        value += 10;
      } else {
        break;
      }
    }

    return value;
  }

  public PlayDto toPrimitives() {
    return new PlayDto(
        id.value(),
        status,
        userId.value(),
        betAmount,
        playerCards,
        dealerCards,
        calculateHandValue(playerCards),
        calculateHandValue(dealerCards));
  }
}
