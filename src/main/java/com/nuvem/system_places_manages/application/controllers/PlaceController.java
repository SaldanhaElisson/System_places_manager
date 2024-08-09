package com.nuvem.system_places_manages.application.controllers;

import com.nuvem.system_places_manages.application.dtos.PlaceDTO;
import com.nuvem.system_places_manages.domain.models.PlaceModel;
import com.nuvem.system_places_manages.domain.services.PlacesServices;
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
    public ResponseEntity<PlaceModel> savePlace(@RequestBody  @Valid PlaceDTO placeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placesServices.create(placeDto));
    }

    @GetMapping
    public ResponseEntity<List<PlaceModel>> getAllPlaces() {
        return ResponseEntity.status(HttpStatus.OK).body(placesServices.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceModel> getPlaceById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(placesServices.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePlace( @PathVariable(value="id") UUID id, @RequestBody  PlaceDTO placeDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(placesServices.update(id, placeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlace(@PathVariable(value="id") UUID id) {

        placesServices.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Place deletado com sucesso");
    }

}
