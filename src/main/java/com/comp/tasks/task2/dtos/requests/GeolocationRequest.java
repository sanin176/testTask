package com.comp.tasks.task2.dtos.requests;

import javax.validation.constraints.NotNull;

public record GeolocationRequest(@NotNull String deviceId,
                                 @NotNull Integer latitude,
                                 @NotNull Integer longitude) {

}
