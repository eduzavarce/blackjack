package dev.eduzavarce.blackjack_api.contexts.shared.infrastructure;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UUIDValidator.class)
@Documented
public @interface ValidUUID {
  String message() default "Invalid UUID format";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean nullable() default false;
}
