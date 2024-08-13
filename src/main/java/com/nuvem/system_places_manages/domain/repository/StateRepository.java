package com.nuvem.system_places_manages.domain.repository;

import com.nuvem.system_places_manages.domain.entity.StateEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StateRepository extends BaseRepository<StateEntity, UUID> {
    boolean existsByUF(String UF);
}
