package com.nuvem.system_places_manages.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    boolean existsByName(String name);
    Optional<T> findByName(String name);
    List<T> findByActiveTrue();
    Optional<T> findByNameAndActiveTrue(String name);
    Optional<T> findByIdAndActiveTrue(UUID id);
}