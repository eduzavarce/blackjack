package dev.eduzavarce.blackjack_api.contexts.shared.infrastructure;

import java.util.UUID;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {

  private boolean nullable;

  @Override
  public void initialize(ValidUUID constraintAnnotation) {
    this.nullable = constraintAnnotation.nullable();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return nullable;
    }

    if (value.trim().isEmpty()) {
      return false;
    }

    try {
      UUID.fromString(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
