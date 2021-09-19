package com.comp.tasks.controller;

import com.comp.tasks.TasksApplicationTests;
import com.comp.tasks.constants.RoleName;
import com.comp.tasks.task2.dtos.GeolocationDto;
import com.comp.tasks.task2.dtos.requests.GeolocationRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GeolocationController extends TasksApplicationTests {

    @Test
    @Order(1)
    public void createGeolocation() {
        GeolocationRequest geolocationRequest = new GeolocationRequest("12345", 1234, 4321);

        log.info("createGeolocation");

        webTestClient.post()
                .uri("/comp/api/v1/geolocation")
                .header("Authorization", getToken(RoleName.ROLE_USER))
                .bodyValue(geolocationRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GeolocationDto.class)
                .consumeWith(response -> {
                    log.info("Success response: {}", response);
                    assertThat(Objects.requireNonNull(response).getResponseBody().getDeviceId()).isEqualTo("12345");
                });
    }

    @Test
    @Order(2)
    public void getGeolocationById() {

        log.info("getGeolocationById");

        webTestClient.get()
                .uri("/comp/api/v1/geolocation/" + "1")
                .header("Authorization", getToken(RoleName.ROLE_USER))
                .exchange()
                .expectStatus().isOk()
                .expectBody(GeolocationDto.class)
                .consumeWith(response -> {
                    log.info("Success response: {}", response);
                    assertThat(Objects.requireNonNull(response).getResponseBody().getLatitude()).isEqualTo(1234);
                });
    }

    @Test
    @Order(3)
    public void updateGeolocation() {
        GeolocationRequest geolocationRequest = new GeolocationRequest("1111", 2222, 3333);

        log.info("updateGeolocation");

        webTestClient.put()
                .uri("/comp/api/v1/geolocation/" + "1")
                .header("Authorization", getToken(RoleName.ROLE_USER))
                .bodyValue(geolocationRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GeolocationDto.class)
                .consumeWith(response -> {
                    log.info("Success response: {}", response);
                    assertThat(Objects.requireNonNull(response).getResponseBody().getDeviceId()).isEqualTo("1111");
                });
    }
}
