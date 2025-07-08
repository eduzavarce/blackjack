package dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain;

import java.util.List;

public record ValidationErrorResponse(List<ValidationError> message) {}
