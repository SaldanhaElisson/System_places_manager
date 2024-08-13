package com.nuvem.system_places_manages;

import com.nuvem.system_places_manages.application.dtos.StateDTO;
import com.nuvem.system_places_manages.domain.repository.StateRepository;
import com.nuvem.system_places_manages.domain.entity.StateEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class StateTests {
    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    public static void setUp(@Autowired StateRepository stateRepository) {
        StateEntity state = new StateEntity();
        state.setName("Rio Grande do Sul");
        state.setUF("RS");
        stateRepository.save(state);
    }

    @Test
    void testCreateStateSuccess() {
        var state = new StateDTO("São Paulo", "SP", null);

        webTestClient.post().uri("/states")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(state)
                .exchange().expectStatus()
                .isCreated().expectBody(StateEntity.class)
                .returnResult();

    }

    @Test
    void testCreateStateWhenItExistFailure() {
        var state = new StateDTO("Amazonas", "AM", null);

        webTestClient.post().uri("/states")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(state)
                .exchange().expectStatus()
                .isCreated().expectBody(StateEntity.class)
                .returnResult();

        webTestClient.post().uri("/states")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(state)
                .exchange().expectStatus()
                .isBadRequest().expectBody()
                .jsonPath("$.errorMessages").value(hasItems("O campo name deve ser único"));

    }

    @Test
    void testGetById() {
        var state = stateRepository.findByNameAndActiveTrue("RIO GRANDE DO SUL").get();
        webTestClient.get().uri("/states/" + state.getId()).exchange().expectStatus().isOk().expectBody(StateEntity.class);
    }

    @Test
    void testGetAll() {
        var expectedStates = stateRepository.findByActiveTrue();

        webTestClient.get()
                .uri("/states")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StateEntity.class)
                .value(states -> {
                    assertThat(states).hasSize(expectedStates.size());
                    assertThat(states).containsAll(expectedStates);
                });
    }

    @Test
    void testDeleteStateSuccess() {
        var state = stateRepository.findByName("RIO GRANDE DO SUL").orElseThrow();
        webTestClient.delete()
                .uri("/states/" + state.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUpdateStateSucess() {
        var state = new StateEntity();
        state.setName("Rio de Janeiro");
        state.setUF("RJ");
        stateRepository.save(state);

        var stateUpdate = new StateDTO("Rio de Janeiro Update", "RJP", null);
        webTestClient.put().uri("/states/" + state.getId())
                .bodyValue(stateUpdate)
                .exchange().expectStatus().isOk();
    }
}