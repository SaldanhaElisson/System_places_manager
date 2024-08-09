package com.nuvem.system_places_manages.application.dtos;

import com.nuvem.system_places_manages.application.validation.UniquePlaceName;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;


public record PlaceDTO(
        @NotBlank @UniquePlaceName String placeName,
        String description,
        LocalDate createDate,
        LocalDate updateDate
)  {

}
