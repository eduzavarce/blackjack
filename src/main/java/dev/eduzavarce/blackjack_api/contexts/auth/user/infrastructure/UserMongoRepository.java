package dev.eduzavarce.blackjack_api.contexts.auth.user.infrastructure;

import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import dev.eduzavarce.blackjack_api.contexts.auth.user.domain.UserRepository;

@Primary
@Repository
public interface UserMongoRepository
    extends ReactiveMongoRepository<UserMongoEntity, String>, UserRepository {}
