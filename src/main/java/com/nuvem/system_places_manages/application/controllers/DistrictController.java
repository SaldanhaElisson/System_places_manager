package com.nuvem.system_places_manages.application.controllers;

import com.nuvem.system_places_manages.application.dtos.DistrictDTO;
import com.nuvem.system_places_manages.application.responses.DistrictResponse;
import com.nuvem.system_places_manages.domain.entity.DistrictEntity;
import com.nuvem.system_places_manages.domain.services.DistrictServices;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/districts")
public class DistrictController {
    private final DistrictServices districtServices;

    public DistrictController(DistrictServices districtServices) {
        this.districtServices = districtServices;
    }

    @PostMapping
    public ResponseEntity<DistrictResponse> saveStateWithCitiesAndDistricts(@RequestBody @Valid DistrictDTO districtDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(districtServices.create(districtDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistrictResponse> getCityById(@PathVariable String id) {

        try {
            UUID uuid = UUID.fromString(id);
            return ResponseEntity.ok(districtServices.getById(uuid));
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }

    @GetMapping
    public ResponseEntity<List<DistrictEntity>> getAllCities() {
        return ResponseEntity.ok(districtServices.getAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlace(@PathVariable(value = "id") String id) {

        try {
            UUID uuid = UUID.fromString(id);
            districtServices.delete(uuid);
            return ResponseEntity.status(HttpStatus.OK).body("District deletado com sucesso");
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<DistrictEntity> updatePlace(@PathVariable(value = "id") String id, @RequestBody DistrictDTO
            districtDTO) {
        try {
            UUID uuid = UUID.fromString(id);
            return ResponseEntity.status(HttpStatus.OK).body(districtServices.update(uuid, districtDTO));
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }

}
