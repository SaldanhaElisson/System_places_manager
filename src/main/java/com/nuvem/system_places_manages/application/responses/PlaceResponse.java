package com.nuvem.system_places_manages.application.responses;

import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.util.UUID;

public record PlaceResponse(UUID id, String name, String description, LocalDate createDate, LocalDate updateDate,
                            String districtName, String cityName, String stateName, Link allPlacesLink) {

}
