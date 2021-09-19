package com.comp.tasks.task2.dtos.mappers;

import com.comp.tasks.task2.db.models.Geolocation;
import com.comp.tasks.task2.dtos.GeolocationDto;
import com.comp.tasks.task2.dtos.requests.GeolocationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GeolocationMapper {

    GeolocationDto toGeolocationDto(Geolocation geolocation);

    Geolocation toGeolocation(GeolocationRequest geolocationRequest);

    void updateGeolocationFromRequest(@MappingTarget Geolocation geolocation,
                                      GeolocationRequest geolocationRequest);

}
