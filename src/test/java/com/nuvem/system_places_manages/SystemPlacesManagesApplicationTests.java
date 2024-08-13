package com.nuvem.system_places_manages;

import com.nuvem.system_places_manages.domain.repository.CityRepository;
import com.nuvem.system_places_manages.domain.repository.DistrictRepository;
import com.nuvem.system_places_manages.domain.repository.PlaceRepository;
import com.nuvem.system_places_manages.application.dtos.PlaceDTO;
import com.nuvem.system_places_manages.domain.repository.StateRepository;
import com.nuvem.system_places_manages.domain.entity.CityEntity;
import com.nuvem.system_places_manages.domain.entity.DistrictEntity;
import com.nuvem.system_places_manages.domain.entity.PlaceEntity;
import com.nuvem.system_places_manages.domain.entity.StateEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.anyOf;


import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class SystemPlacesManagesApplicationTests {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PlaceRepository placeRepository;

    @BeforeAll
    public static void setUp(@Autowired StateRepository stateRepository,
                             @Autowired CityRepository cityRepository,
                             @Autowired DistrictRepository districtRepository) {

        // Cria e salva um estado
        StateEntity state = new StateEntity();
        state.setName("Ceará");
        state.setUF("CE");
        stateRepository.save(state);

        // Cria e salva uma cidade
        CityEntity city = new CityEntity();
        city.setName("Fortaleza");
        city.setState(state);
        cityRepository.save(city);

        // Cria e salva um bairro
        DistrictEntity district01 = new DistrictEntity();
        district01.setName("Aldeota");
        district01.setCity(city);
        districtRepository.save(district01);

        DistrictEntity district02 = new DistrictEntity();
        district02.setName("Novo Maracanaú");
        district02.setCity(city);
        districtRepository.save(district02);
    }


    @Test
    void testCreatePlaceSucess() {
        var place01 = new PlaceDTO("Disney", "Lugar muito bonito", null, null, "Aldeota");

        webTestClient.post()
                .uri("/places")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(place01)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(place01.name())
                .jsonPath("$.description").isEqualTo(place01.description())
                .jsonPath("$.createDate").isEqualTo(LocalDate.now().toString())
                .jsonPath("$.updateDate").isEmpty()
                .jsonPath("$.districtName").isEqualTo("ALDEOTA")
                .jsonPath("$.cityName").isEqualTo("FORTALEZA")
                .jsonPath("$.stateName").isEqualTo("CEARÁ")
        ;
    }

    @Test
    void testeCreatePlaceWhenFullFieldsAreVoidFailure() {
        var place01 = new PlaceDTO("", "", null, null, "");

        webTestClient.post()
                .uri("/places")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(place01)
                .exchange()
                .expectStatus().isBadRequest().expectBody().jsonPath("$.errorMessages").value(anyOf(
                        hasItem("O campo name não deve estar em branco"),
                        hasItem("O campo districtName não deve estar em branco")
                ));

    }

    @Test
    void testCreatePlaceWhenFieldDescriptionisVoidSucess() {
        var place01 = new PlaceDTO("Batata é bom demais", "", null, null,
                "Aldeota");

        webTestClient.post()
                .uri("/places")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(place01)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(place01.name())
                .jsonPath("$.description").isEmpty()
                .jsonPath("$.createDate").isEqualTo(LocalDate.now().toString())
                .jsonPath("$.updateDate").isEmpty()
                .jsonPath("$.districtName").isEqualTo("ALDEOTA")
                .jsonPath("$.cityName").isEqualTo("FORTALEZA")
                .jsonPath("$.stateName").isEqualTo("CEARÁ");
    }

    @Test
    void testCreatePlaceWhenHasTwoPlacesWithSameNameFailure() {
        var place01 = new PlaceDTO("Vila das Flores", "Perto de pacatuba", null, null,
                "Aldeota");


        webTestClient.post()
                .uri("/places")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(place01)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri("/places")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(place01)
                .exchange()
                .expectStatus().isBadRequest().expectBody()
                .jsonPath("$.errorMessages").value(hasItems("O campo name deve ser único"))
        ;
    }


    private boolean isSortedByCreateDate(List<PlaceEntity> places) {
        for (int i = 1; i < places.size(); i++) {
            if (places.get(i - 1).getCreateDate().isAfter(places.get(i).getCreateDate())) {
                return false;
            }
        }
        return true;
    }

    @Test
    void testGetAllPlacesOrderByCreateDataSucess() {

        var district = districtRepository.findByName("ALDEOTA").get();

        placeRepository.save(new PlaceEntity("Place A", "Description A",
                LocalDate.of(2023, 1, 1), null, district));
        placeRepository.save(new PlaceEntity("Place B", "Description B",
                LocalDate.of(2023, 2, 1), null, district));
        placeRepository.save(new PlaceEntity("Place C", "Description C",
                LocalDate.of(2023, 3, 1), null, district));

        List<PlaceEntity> places = webTestClient.get()
                .uri("/places")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PlaceEntity.class)
                .returnResult()
                .getResponseBody();

        assertTrue(isSortedByCreateDate(places));

    }

    @Test
    void testGetById() {

        var district = districtRepository.findByName("ALDEOTA").get();
        var place01 = placeRepository.save(new PlaceEntity("Place J", "Description J",
                LocalDate.of(2023, 1, 1), null, district));

        webTestClient.get().uri("/places/" + place01.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(place01.getName())
                .jsonPath("$.description").isEqualTo(place01.getDescription())
                .jsonPath("$.createDate").isEqualTo(place01.getCreateDate().toString())
                .jsonPath("$.updateDate").isEmpty()
                .jsonPath("$.districtName").isEqualTo("ALDEOTA")
                .jsonPath("$.cityName").isEqualTo("FORTALEZA")
                .jsonPath("$.stateName").isEqualTo("CEARÁ");


    }

    @Test
    void testGetByIdWheNotFoundId() {

        var district = districtRepository.findByName("ALDEOTA").get();
        placeRepository.save(new PlaceEntity("Place K", "Description K",
                LocalDate.of(2023, 1, 1), null, district));

        webTestClient.get().uri("/places/cd5e81c6-05bf-44cf-b9b4-34c3021bdf7")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Id não encontrado.");

    }


    @Test
    void testDeleteWithIdNotFoundFailure() {
        webTestClient.delete()
                .uri("/places/cd5e81c6-05bf-44cf-b9b4-34c3021bdf7")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound().expectBody().jsonPath("$.message")
                .isEqualTo("Place não encontrado");
    }

    @Test
    void testDeleteSucess() {
        var place01 = placeRepository.save(new PlaceEntity("Place D", "Description A",
                LocalDate.of(2023, 1, 1), null,
                districtRepository.findByName("ALDEOTA").get()));

        webTestClient.delete().uri("/places/" + place01.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUpdateWithoutPlaceNameFieldSucess() {

        var district = districtRepository.findByName("ALDEOTA").get();

        var place01 = placeRepository.save(new PlaceEntity("Place E", "Description A",
                LocalDate.of(2023, 1, 1), null, district));

        var place01Updated = new PlaceDTO(null, "Description updated", null, null,
                "Novo Maracanaú");

        webTestClient.put().uri("/places/" + place01.getId())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(place01Updated)
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(place01.getName())
                .jsonPath("$.description").isEqualTo(place01Updated.description())
                .jsonPath("$.createDate").isEqualTo(place01.getCreateDate().toString())
                .jsonPath("$.updateDate").isEqualTo(LocalDate.now().toString())
                .jsonPath("$.districtName").isEqualTo("NOVO MARACANAÚ")
                .jsonPath("$.cityName").isEqualTo("FORTALEZA")
                .jsonPath("$.stateName").isEqualTo("CEARÁ");
    }

    @Test
    void testUpdateWithoutDescriptionFieldSucess() {

        var district = districtRepository.findByName("ALDEOTA").get();
        var place01 = placeRepository.save(new PlaceEntity("Place F", "Description F",
                LocalDate.of(2023, 1, 1), null, district));

        var place01Updated = new PlaceDTO("Place F update name", null, null, null,
                "Novo Maracanaú");

        webTestClient.put().uri("/places/" + place01.getId())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(place01Updated)
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(place01Updated.name())
                .jsonPath("$.description").isEqualTo(place01.getDescription())
                .jsonPath("$.updateDate").isEqualTo(LocalDate.now().toString())
                .jsonPath("$.createDate").isEqualTo(place01.getCreateDate().toString())
                .jsonPath("$.districtName").isEqualTo("NOVO MARACANAÚ")
                .jsonPath("$.cityName").isEqualTo("FORTALEZA")
                .jsonPath("$.stateName").isEqualTo("CEARÁ");
    }

    @Test
    void testUpdateFullFields() {

        var district = districtRepository.findByName("ALDEOTA").get();
        var place01 = placeRepository.save(new PlaceEntity("Place G", "Description G",
                LocalDate.of(2023, 1, 1), null, district));

        var place01Updated = new PlaceDTO("Place G update name", "Description G update",
                null, null, "Novo Maracanaú");

        webTestClient.put().uri("/places/" + place01.getId())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(place01Updated)
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(place01Updated.name())
                .jsonPath("$.description").isEqualTo(place01Updated.description())
                .jsonPath("$.updateDate").isEqualTo(LocalDate.now().toString())
                .jsonPath("$.createDate").isEqualTo(place01.getCreateDate().toString())
                .jsonPath("$.districtName").isEqualTo("NOVO MARACANAÚ")
                .jsonPath("$.cityName").isEqualTo("FORTALEZA")
                .jsonPath("$.stateName").isEqualTo("CEARÁ");

    }

    @Test
    void testUpdateWhenNotFoundDistrict() {
        var district = districtRepository.findByName("ALDEOTA").get();
        var place01 = placeRepository.save(new PlaceEntity("Place H", "Description H",
                LocalDate.of(2023, 1, 1), null, district));

        var place01Updated = new PlaceDTO("Place G update name", "Description G update",
                null, null, "Nova ponte");

        webTestClient.put().uri("/places/" + place01.getId())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(place01Updated)
                .exchange().expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("District não encontrado: " + place01Updated.districtName());
    }

    @Test
    void testUpdateWhenNotFoundPlace() {
        var district = districtRepository.findByName("ALDEOTA").get();
        placeRepository.save(new PlaceEntity("Place I", "Description I",
                LocalDate.of(2023, 1, 1), null, district));

        var place01Updated = new PlaceDTO("Place G update name", "Description G update",
                null, null, "Aldeota");

        webTestClient.put().uri("/places/cd5e81c6-05bf-44cf-b9b4-34c3021bdf7")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(place01Updated)
                .exchange().expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Id não encontrado.");
    }
}
