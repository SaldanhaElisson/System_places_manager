package com.nuvem.system_places_manages.domain.repository;

import com.nuvem.system_places_manages.domain.entity.CityEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CityRepository extends BaseRepository<CityEntity, UUID> {

}
