package com.nuvem.system_places_manages.application.controllers;

import com.nuvem.system_places_manages.application.dtos.PlaceDTO;
import com.nuvem.system_places_manages.application.responses.PlaceResponse;
import com.nuvem.system_places_manages.domain.services.PlacesServices;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlacesServices placesServices;

    public PlaceController(PlacesServices placesServices) {
        this.placesServices = placesServices;
    }

    @PostMapping
    public ResponseEntity<PlaceResponse> savePlace(@RequestBody @Valid PlaceDTO placeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placesServices.create(placeDto));
    }

    @GetMapping
    public ResponseEntity<List<PlaceResponse>> getAllPlaces() {
        return ResponseEntity.status(HttpStatus.OK).body(placesServices.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable String id) {

        try {
            UUID uuid = UUID.fromString(id);
            return ResponseEntity.status(HttpStatus.OK).body(placesServices.getById(uuid));
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlace(@PathVariable(value = "id") String id) {

        try {
            UUID uuid = UUID.fromString(id);
            placesServices.delete(uuid);
            return ResponseEntity.status(HttpStatus.OK).body("Place deletado com sucesso");
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceResponse> updatePlace(@PathVariable(value = "id") String id, @RequestBody PlaceDTO placeDTO) {
        try {
            UUID uuid = UUID.fromString(id);
            return ResponseEntity.status(HttpStatus.OK).body(placesServices.update(uuid, placeDTO));
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Id não encontrado.");
        }
    }


}
