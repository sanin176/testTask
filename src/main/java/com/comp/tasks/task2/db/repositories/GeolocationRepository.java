package com.comp.tasks.task2.db.repositories;

import com.comp.tasks.task2.db.models.Geolocation;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface GeolocationRepository extends R2dbcRepository<Geolocation, Long> {

    Mono<Geolocation> findByIdAndLogin(Long id, String login);

}
