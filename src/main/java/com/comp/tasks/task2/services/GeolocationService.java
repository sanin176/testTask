package com.comp.tasks.task2.services;

import com.comp.tasks.security.exceptions.BadRequestException;
import com.comp.tasks.security.jwt.CompAuthenticationToken;
import com.comp.tasks.task2.db.models.Geolocation;
import com.comp.tasks.task2.db.repositories.GeolocationRepository;
import com.comp.tasks.task2.dtos.GeolocationDto;
import com.comp.tasks.task2.dtos.mappers.GeolocationMapper;
import com.comp.tasks.task2.dtos.requests.GeolocationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeolocationService {

    GeolocationRepository geolocationRepository;

    GeolocationMapper geolocationMapper;

    /*
     * TODO
     *  if correct, then you need to contact another authorization service,
     *  for example via GRPS, to get the user id in order to bind this
     *  record to it, but I do it without microservices, so
     *  I will keep the username instead of id
     * */
    public Mono<GeolocationDto> getGeolocation(Long geolocationId,
                                               CompAuthenticationToken compAuthenticationToken) {
        return geolocationRepository.findByIdAndLogin(geolocationId, compAuthenticationToken.getEmail())
                .map(geolocationMapper::toGeolocationDto);
    }

    /*
     * TODO
     *  if correct, then you need to contact another authorization service,
     *  for example via GRPS, to get the user id in order to bind this
     *  record to it, but I do it without microservices, so
     *  I will keep the username instead of id
     * */
    public Mono<GeolocationDto> createGeological(GeolocationRequest geolocationRequest,
                                                 CompAuthenticationToken compAuthenticationToken) {
        Geolocation geolocation = geolocationMapper.toGeolocation(geolocationRequest);
        geolocation.setLogin(compAuthenticationToken.getEmail());
        return geolocationRepository.save(geolocation)
                .map(geolocationMapper::toGeolocationDto);
    }

    /*
     * TODO
     *  if correct, then you need to contact another authorization service,
     *  for example via GRPS, to get the user id in order to bind this
     *  record to it, but I do it without microservices, so
     *  I will keep the username instead of id
     * */
    public Mono<GeolocationDto> updateGeological(Long geolocationId, GeolocationRequest geolocationRequest,
                                                 CompAuthenticationToken compAuthenticationToken) {
        return geolocationRepository.findByIdAndLogin(geolocationId, compAuthenticationToken.getEmail())
                .switchIfEmpty(Mono.error(new BadRequestException("Geolocation does not exists.")))
                .flatMap(geolocation -> {
                    geolocationMapper.updateGeolocationFromRequest(geolocation, geolocationRequest);
                    return geolocationRepository.save(geolocation);
                })
                .map(geolocationMapper::toGeolocationDto);
    }
}
