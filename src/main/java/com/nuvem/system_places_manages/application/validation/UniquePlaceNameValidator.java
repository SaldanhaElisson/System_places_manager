package com.nuvem.system_places_manages.application.validation;

import com.nuvem.system_places_manages.domain.Repository.PlaceRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniquePlaceNameValidator implements ConstraintValidator<UniquePlaceName, String> {

    private final PlaceRepository placeRepository;

    public UniquePlaceNameValidator(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public boolean isValid(String placeName, ConstraintValidatorContext context) {
        return placeName != null && !placeRepository.existsByPlaceName(placeName);
    }

}
