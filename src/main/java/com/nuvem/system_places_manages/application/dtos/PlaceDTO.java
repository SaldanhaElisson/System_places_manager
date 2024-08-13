package com.nuvem.system_places_manages.application.dtos;

import com.nuvem.system_places_manages.application.validations.UniqueName;
import com.nuvem.system_places_manages.domain.repository.PlaceRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record PlaceDTO(
        @NotBlank @NotNull @UniqueName(repository = PlaceRepository.class) String name,
        String description,
        LocalDate createDate,
        LocalDate updateDate,
        @NotBlank @NotNull String districtName
) {
    public PlaceDTO {
        districtName = districtName.toUpperCase();
    }
}
