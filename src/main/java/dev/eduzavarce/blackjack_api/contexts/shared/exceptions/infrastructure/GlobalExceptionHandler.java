package dev.eduzavarce.blackjack_api.contexts.shared.exceptions.infrastructure;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import dev.eduzavarce.blackjack_api.contexts.shared.exceptions.domain.domain.*;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleNotFound(NotFoundException e) {
    ErrorResponse body = new ErrorResponse(e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(body));
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleAlreadyExists(AlreadyExistsException e) {
    ErrorResponse body = new ErrorResponse(e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(body));
  }

  @ExceptionHandler(EmptyFieldException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleEmptyField(EmptyFieldException e) {
    ErrorResponse body = new ErrorResponse(e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body));
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ResponseEntity<ValidationErrorResponse>> handleValidationException(
      WebExchangeBindException e) {
    List<ValidationError> errors =
        e.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
            .toList();
    ValidationErrorResponse body = new ValidationErrorResponse(errors);
    return Mono.just(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body));
  }

  @ExceptionHandler(CustomException.class)
  public Mono<ResponseEntity<ErrorResponse>> handlePlayerExists(CustomException e) {
    ErrorResponse body = new ErrorResponse(e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorResponse>> handleGeneric(Exception e) {
    ErrorResponse body = new ErrorResponse(e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body));
  }
}
