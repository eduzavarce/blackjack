package dev.eduzavarce.blackjack_api.contexts.shared.domain;

public record DomainEventDto(
    String eventName, String occurredOn, String aggregateId, Record body) {}
