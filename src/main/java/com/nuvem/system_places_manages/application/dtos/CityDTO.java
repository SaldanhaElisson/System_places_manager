package com.nuvem.system_places_manages.application.dtos;

import com.nuvem.system_places_manages.application.validations.UniqueName;
import com.nuvem.system_places_manages.domain.repository.CityRepository;
import jakarta.validation.constraints.NotBlank;


import java.util.Set;

public record CityDTO(
        @NotBlank @UniqueName(repository = CityRepository.class) String name,
        @NotBlank String stateName,
        Set<DistrictDTO> districts
) {

    public CityDTO {
        name = name.toUpperCase();
        stateName = stateName.toUpperCase();
    }
}
