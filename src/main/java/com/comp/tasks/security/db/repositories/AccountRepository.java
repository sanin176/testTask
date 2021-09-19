package com.comp.tasks.security.db.repositories;

import com.comp.tasks.security.db.models.Account;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends R2dbcRepository<Account, Long> {
    Mono<Account> findByLogin(String login);
}
