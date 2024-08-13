package com.nuvem.system_places_manages.domain.repository;


import com.nuvem.system_places_manages.domain.entity.DistrictEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DistrictRepository extends BaseRepository<DistrictEntity, UUID> {

}
