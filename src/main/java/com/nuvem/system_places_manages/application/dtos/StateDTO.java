package com.nuvem.system_places_manages.application.dtos;

import com.nuvem.system_places_manages.application.validations.UniqueName;
import com.nuvem.system_places_manages.domain.repository.StateRepository;
import com.nuvem.system_places_manages.domain.entity.CityEntity;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record StateDTO(
        @NotBlank @UniqueName(repository = StateRepository.class) String name,
        @NotBlank String UF,
        Set<CityEntity> cities
) {
    public StateDTO {
        name = name.toUpperCase();
        UF = UF.toUpperCase();
    }

}
