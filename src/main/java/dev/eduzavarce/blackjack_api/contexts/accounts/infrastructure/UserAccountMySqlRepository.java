package dev.eduzavarce.blackjack_api.contexts.accounts.infrastructure;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import dev.eduzavarce.blackjack_api.contexts.accounts.domain.UserAccountRepository;

@Repository
public interface UserAccountMySqlRepository
    extends R2dbcRepository<UserAccountMySqlEntity, String>, UserAccountRepository {}
