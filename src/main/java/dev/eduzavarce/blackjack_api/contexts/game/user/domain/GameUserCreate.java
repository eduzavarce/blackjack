package dev.eduzavarce.blackjack_api.contexts.game.user.domain;

import java.io.Serializable;
import java.util.List;

public record GameUserCreate(String id, String name, double balance, List<String> blackjackGames)
    implements Serializable {}
