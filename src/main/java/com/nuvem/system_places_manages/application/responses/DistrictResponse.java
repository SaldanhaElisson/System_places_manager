package com.nuvem.system_places_manages.application.responses;

import java.util.UUID;

public record DistrictResponse(
        UUID id,
        String name,
        String cityName
) {

}