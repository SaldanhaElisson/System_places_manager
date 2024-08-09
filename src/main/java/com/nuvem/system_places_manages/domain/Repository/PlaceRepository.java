package com.nuvem.system_places_manages.domain.Repository;

import com.nuvem.system_places_manages.domain.models.PlaceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceModel, UUID> {
    boolean existsByPlaceName(String placeName);
}
