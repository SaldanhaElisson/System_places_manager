package com.nuvem.system_places_manages.application.controllers;

import com.nuvem.system_places_manages.application.dtos.DistrictDTO;
import com.nuvem.system_places_manages.application.dtos.StateDTO;
import com.nuvem.system_places_manages.domain.entity.DistrictEntity;
import com.nuvem.system_places_manages.domain.entity.StateEntity;
import com.nuvem.system_places_manages.domain.services.StateServices;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/states")
public class StateController {

    private final StateServices stateServices;

    public StateController(StateServices stateServices) {
        this.stateServices = stateServices;
    }

    @PostMapping
    public ResponseEntity<StateEntity> saveStateWithCitiesAndDistricts(@RequestBody @Valid StateDTO stateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stateServices.create(stateDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<StateEntity> getCityById(@PathVariable String id) {

        try {
            UUID uuid = UUID.fromString(id);
            return ResponseEntity.ok(stateServices.getById(uuid));
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }

    @GetMapping
    public ResponseEntity<List<StateEntity>> getAllCities() {
        return ResponseEntity.ok(stateServices.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlace(@PathVariable(value = "id") String id) {
        try {
            UUID uuid = UUID.fromString(id);
            stateServices.delete(uuid);
            return ResponseEntity.status(HttpStatus.OK).body("State deletado com sucesso");
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StateEntity> updatePlace(@PathVariable(value = "id") String id, @RequestBody StateDTO
            stateDTO) {
        try {
            UUID uuid = UUID.fromString(id);
            return ResponseEntity.status(HttpStatus.OK).body(stateServices.update(uuid, stateDTO));
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }
}
