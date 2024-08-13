package com.nuvem.system_places_manages;

import com.nuvem.system_places_manages.application.dtos.CityDTO;
import com.nuvem.system_places_manages.domain.repository.CityRepository;
import com.nuvem.system_places_manages.domain.repository.StateRepository;
import com.nuvem.system_places_manages.domain.entity.CityEntity;
import com.nuvem.system_places_manages.domain.entity.StateEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class CityTests {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private StateRepository stateRepository;

    @BeforeAll
    public static void setUp(@Autowired StateRepository stateRepository,
                             @Autowired CityRepository cityRepository) {
        StateEntity state = new StateEntity();
        state.setName("BRASILIA");
        state.setUF("BL");
        stateRepository.save(state);

        CityEntity city = new CityEntity();
        city.setName("CIDADE NOVA");
        city.setState(state);
        cityRepository.save(city);
    }

    @Test
    void testCreateCitySuccess() {

        var city = new CityDTO("Gramado", "Brasilia", null);

        webTestClient.post().uri("/cities")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(city)
                .exchange().expectStatus()
                .isCreated().expectBody(CityEntity.class)
                .returnResult();
    }

    @Test
    void testCityGetById() {
        var city = cityRepository.findByNameAndActiveTrue("GRAMADO").get();
        webTestClient.get().uri("/cities/" + city.getId()).exchange()
                .expectStatus()
                .isOk()
                .expectBody(StateEntity.class);
    }

    @Test
    void testCityGetAll() {
        var expectedCities = cityRepository.findByActiveTrue();

        webTestClient.get()
                .uri("/cities")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CityEntity.class)
                .value(states -> {
                    assertThat(states).hasSize(expectedCities.size());
                    assertThat(states).containsAll(expectedCities);
                });
    }

    @Test
    void testDeleteCitySuccess() {
        var city = cityRepository.findByName("CIDADE NOVA").orElseThrow();
        webTestClient.delete()
                .uri("/cities/" + city.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void updateCitySuccess() {
        var state = stateRepository.findByNameAndActiveTrue("BRASILIA").get();
        var city = new CityEntity();
        city.setName("CIDADE NOVA2");
        city.setState(state);
        cityRepository.save(city);

        var cityUpdate = new CityDTO("Rio de Janeiro Update", "RJP", null);

        webTestClient.put().uri("/cities/" + city.getId())
                .bodyValue(cityUpdate)
                .exchange().expectStatus().isOk();

    }

}
