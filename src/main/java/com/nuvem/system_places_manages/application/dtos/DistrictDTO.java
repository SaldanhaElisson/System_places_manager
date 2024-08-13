package com.nuvem.system_places_manages.application.dtos;

import com.nuvem.system_places_manages.application.validations.UniqueName;
import com.nuvem.system_places_manages.domain.repository.DistrictRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;


public record DistrictDTO(
        @NotBlank @UniqueName(repository = DistrictRepository.class) String name,
        @NotBlank @NotNull String cityName,
        Set<PlaceDTO> places
) {

    public DistrictDTO {
        name = name.toUpperCase();
        cityName = cityName.toUpperCase();
    }
}
