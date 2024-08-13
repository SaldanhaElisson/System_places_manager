package com.nuvem.system_places_manages.application.controllers;

import com.nuvem.system_places_manages.application.dtos.CityDTO;
import com.nuvem.system_places_manages.domain.entity.CityEntity;
import com.nuvem.system_places_manages.domain.services.CityServices;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cities")
public class CityController {
    private final CityServices cityServices;

    public CityController(CityServices cityServices) {
        this.cityServices = cityServices;
    }

    @PostMapping
    public ResponseEntity<CityEntity> saveStateWithCitiesAndDistricts(@RequestBody @Valid CityDTO cityDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityServices.create(cityDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityEntity> getCityById(@PathVariable String id) {

        try {
            UUID uuid = UUID.fromString(id);
            return ResponseEntity.ok(cityServices.getById(uuid));
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }

    @GetMapping
    public ResponseEntity<List<CityEntity>> getAllCities() {
        return ResponseEntity.ok(cityServices.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlace(@PathVariable(value = "id") String id) {

        try {
            UUID uuid = UUID.fromString(id);
            cityServices.delete(uuid);
            return ResponseEntity.status(HttpStatus.OK).body("City deletado com sucesso");
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityEntity> updatePlace(@PathVariable(value = "id") String id, @RequestBody CityDTO cityDTO) {
        try {
            UUID uuid = UUID.fromString(id);
            return ResponseEntity.status(HttpStatus.OK).body(cityServices.update(uuid, cityDTO));
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }

}
