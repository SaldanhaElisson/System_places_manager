package com.nuvem.system_places_manages.domain.repository;

import com.nuvem.system_places_manages.domain.entity.PlaceEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaceRepository extends BaseRepository<PlaceEntity, UUID> {

}
