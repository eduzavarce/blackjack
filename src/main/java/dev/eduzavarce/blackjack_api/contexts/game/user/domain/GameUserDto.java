package dev.eduzavarce.blackjack_api.contexts.game.user.domain;

import java.util.List;

public record GameUserDto(String id, String name, double balance, List<String> blackjackGames) {}
