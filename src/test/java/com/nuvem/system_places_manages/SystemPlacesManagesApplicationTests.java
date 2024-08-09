package com.nuvem.system_places_manages;

import com.nuvem.system_places_manages.domain.Repository.PlaceRepository;
import com.nuvem.system_places_manages.application.dtos.PlaceDTO;
import com.nuvem.system_places_manages.domain.models.PlaceModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class SystemPlacesManagesApplicationTests {
	@Autowired
	private WebTestClient webTestClient;
    @Autowired
    private PlaceRepository placeRepository;

	@Test
	void testCreatePlaceSucess() {
		var place01 = new PlaceDTO("Dona batata", "batata é bom", null, null);

		webTestClient.post()
				.uri("/places")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(place01)
				.exchange()
				.expectStatus().isCreated()
				.expectBody()
				.jsonPath("$.placeName").isEqualTo(place01.placeName())
				.jsonPath("$.description").isEqualTo(place01.description())
				.jsonPath("$.createDate").isNotEmpty()
				.jsonPath("$.updateDate").isEmpty();
	}

	@Test
	void testeCreatePlaceWhenFullFielsAreVoidFailure() {
		var place01 = new PlaceDTO("", "", null, null);

		webTestClient.post()
				.uri("/places")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(place01)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody()
				.jsonPath("$.errorMessages").value(hasItems(
						"O campo placeName não deve estar em branco"
				));
	}

	@Test
	void testCreatePlaceWhenFieldDescriptionFailure(){
		var place01 = new PlaceDTO("Batata é bom demais", "", null, null);

		webTestClient.post()
				.uri("/places")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(place01)
				.exchange()
				.expectStatus().isCreated()
				.expectBody()
				.jsonPath("$.placeName").isEqualTo(place01.placeName())
				.jsonPath("$.description").isEmpty()
				.jsonPath("$.createDate").isEqualTo(LocalDate.now().toString())
				.jsonPath("$.updateDate").isEmpty();
	}

	@Test
	void testCreatePlaceWhenHasTwoPlacesWithSameNameFailure(){
		var place01 = new PlaceDTO("Vila das Flores", "Perto de pacatuba", null, null);


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
				.expectStatus().isBadRequest().expectBody().jsonPath("$.errorMessages").value(hasItems("O campo placeName deve ser único"))
		;
	}


	private boolean isSortedByCreateDate(List<PlaceModel> places) {
		for (int i = 1; i < places.size(); i++) {
			if (places.get(i - 1).getCreateDate().isAfter(places.get(i).getCreateDate())) {
				return false;
			}
		}
		return true;
	}

	@Test
	void testOrderByDataSucess(){
		placeRepository.save(new PlaceModel("Place A", "Description A",
				LocalDate.of(2023, 1, 1), null));
		placeRepository.save(new PlaceModel("Place B", "Description B",
				LocalDate.of(2023, 2, 1), null));
		placeRepository.save(new PlaceModel("Place C", "Description C",
				LocalDate.of(2023, 3, 1), null));

		List<PlaceModel> places = webTestClient.get()
				.uri("/places")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(PlaceModel.class)
				.returnResult()
				.getResponseBody();

		assertTrue(isSortedByCreateDate(places));

	}

	@Test
	void testDeleteWithIdNotFoundFailure(){
		webTestClient.delete()
				.uri("/places/cd5e81c6-05bf-44cf-b9b4-34c3021bdf7")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound().expectBody().jsonPath("$.message")
				.isEqualTo("Entidade não encontrada");
	}


	@Test
	void testDeleteSucess(){
		var place01 = placeRepository.save(new PlaceModel("Place D", "Description A",
				LocalDate.of(2023, 1, 1), null));

		webTestClient.delete().uri("/places/"+ place01.getUuid())
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	void testUpdateWithoutPlaceNameFieldSucess(){
		var place01 = placeRepository.save(new PlaceModel("Place E", "Description A",
				LocalDate.of(2023, 1, 1), null));

		var place01Updated = new PlaceDTO(null, "Description updated", null, null);

		webTestClient.put().uri("/places/" + place01.getUuid())
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(place01Updated)
				.exchange().expectStatus().isOk()
				.expectBody()
				.jsonPath("$.placeName").isEqualTo(place01.getPlaceName())
				.jsonPath("$.description").isEqualTo(place01Updated.description())
				.jsonPath("$.updateDate").isEqualTo(LocalDate.now().toString())
				.jsonPath("$.createDate").isEqualTo(place01.getCreateDate().toString())
				;
		;
	}

	@Test
	void testUpdateWithoutDescriptionFieldSucess(){
		var place01 = placeRepository.save(new PlaceModel("Place F", "Description F",
				LocalDate.of(2023, 1, 1), null));

		var place01Updated = new PlaceDTO("Place F update name", null , null, null);

		webTestClient.put().uri("/places/" + place01.getUuid())
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(place01Updated)
				.exchange().expectStatus().isOk()
				.expectBody()
				.jsonPath("$.placeName").isEqualTo(place01Updated.placeName())
				.jsonPath("$.description").isEqualTo(place01.getDescription())
				.jsonPath("$.updateDate").isEqualTo(LocalDate.now().toString())
				.jsonPath("$.createDate").isEqualTo(place01.getCreateDate().toString());
		;
	}

	@Test
	void testUpdateFullFields(){
		var place01 = placeRepository.save(new PlaceModel("Place G", "Description G",
				LocalDate.of(2023, 1, 1), null));

		var place01Updated = new PlaceDTO("Place G update name", "Description G update" ,
				null, null);

		webTestClient.put().uri("/places/" + place01.getUuid())
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(place01Updated)
				.exchange().expectStatus().isOk()
				.expectBody()
				.jsonPath("$.placeName").isEqualTo(place01Updated.placeName())
				.jsonPath("$.description").isEqualTo(place01Updated.description())
				.jsonPath("$.updateDate").isEqualTo(LocalDate.now().toString())
				.jsonPath("$.createDate").isEqualTo(place01.getCreateDate().toString());
	}
}
