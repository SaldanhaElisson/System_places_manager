package com.nuvem.system_places_manages;

import com.nuvem.system_places_manages.application.dtos.DistrictDTO;
import com.nuvem.system_places_manages.domain.repository.CityRepository;
import com.nuvem.system_places_manages.domain.repository.DistrictRepository;
import com.nuvem.system_places_manages.domain.repository.StateRepository;
import com.nuvem.system_places_manages.domain.entity.CityEntity;
import com.nuvem.system_places_manages.domain.entity.DistrictEntity;
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
public class DistrictTests {

    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    public static void setUp(@Autowired StateRepository stateRepository, @Autowired CityRepository cityRepository,
                             @Autowired DistrictRepository districtRepository) {
        StateEntity state = new StateEntity();
        state.setName("Rio Grande do Sul2");
        state.setUF("RS2");
        stateRepository.save(state);

        CityEntity city = new CityEntity();
        city.setName("CIDADE NOVA3");
        city.setState(state);
        cityRepository.save(city);

        DistrictEntity district = new DistrictEntity();
        district.setName("Bairro novo");
        city.addDistrict(district);
        districtRepository.save(district);
    }

    @Test
    void testCreateDistrictSuccess() {

        var district = new DistrictDTO("Floresta", "Cidade nova3", null);

        webTestClient.post().uri("/districts").contentType(MediaType.APPLICATION_JSON).bodyValue(district).exchange()
                .expectStatus().isCreated().expectBody(DistrictEntity.class).returnResult();
    }

    @Test
    void testDistrictGetById() {
        var district = districtRepository.findByNameAndActiveTrue("BAIRRO NOVO").get();
        webTestClient.get().uri("/districts/" + district.getId()).exchange().expectStatus().isOk().expectBody(DistrictEntity.class);
    }

    @Test
    void testDistrictGetAll() {
        var expectedDistricts = districtRepository.findByActiveTrue();

        webTestClient.get().uri("/districts").exchange().expectStatus().isOk().expectBodyList(DistrictEntity.class).value(states -> {
            assertThat(states).hasSize(expectedDistricts.size());
            assertThat(states).containsAll(expectedDistricts);
        });
    }

    @Test
    void testDeleteDistrictSuccess() {
        var district = districtRepository.findByName("BAIRRO NOVO").orElseThrow();
        webTestClient.delete().uri("/districts/" + district.getId()).exchange().expectStatus().isOk();
    }

    @Test
    void testUpdateDistrictSuccess() {
        var city = cityRepository.findByNameAndActiveTrue("CIDADE NOVA3").get();
        var district = new DistrictEntity();
        district.setName("Pajuçara");
        district.setCity(city);
        districtRepository.save(district);

        var stateUpdate = new StateEntity();
        stateUpdate.setName("Rio de Janeiro3");
        stateUpdate.setUF("RJ3");
        stateRepository.save(stateUpdate);

        var districtUpdate = new DistrictDTO("Novo Pajuçara", "Rio de Janeiro3", null);

        webTestClient.put().uri("/districts/" + district.getId()).bodyValue(districtUpdate).exchange().expectStatus().isOk();
    }

}
