package com.comp.tasks.task3.db.repositories;

import com.comp.tasks.task3.db.models.Invoice;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface InvoiceRepository extends R2dbcRepository<Invoice, Long> {

    Mono<Invoice> findByLogin(String login);

}
