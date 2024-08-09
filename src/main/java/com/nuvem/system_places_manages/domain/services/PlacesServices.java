package com.nuvem.system_places_manages.domain.services;

import com.nuvem.system_places_manages.domain.Repository.PlaceRepository;
import com.nuvem.system_places_manages.application.controllers.PlaceController;
import com.nuvem.system_places_manages.application.dtos.PlaceDTO;
import com.nuvem.system_places_manages.domain.models.PlaceModel;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.*;

@Service
public class PlacesServices {
    private final PlaceRepository placeRepository;
    private final String MESSAGE_NOT_FOUND_ENTITY = "Entidade não encontrada";


    public PlacesServices(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public PlaceModel create(PlaceDTO placeDto) {
        PlaceModel placeModel = new PlaceModel();
        BeanUtils.copyProperties(placeDto, placeModel);

        placeModel.setCreateDate(LocalDate.now());

        placeRepository.save(placeModel);
        placeModel.add(linkTo(methodOn(PlaceController.class).getAllPlaces()).withSelfRel());
        return placeModel;

    }

    public List<PlaceModel> getAll() {
        List<PlaceModel> places = placeRepository.findAll();


        if (!places.isEmpty()) {
            for (PlaceModel place : places) {
                UUID id = place.getUuid();
                place.add(linkTo(methodOn(PlaceController.class).getPlaceById(id)).withSelfRel());
            }
        }
        places.sort(Comparator.comparing(PlaceModel::getCreateDate));
        return places;
    }

    public PlaceModel getById(UUID id) {
        var placeModel = placeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MESSAGE_NOT_FOUND_ENTITY));

        placeModel.add(linkTo(methodOn(PlaceController.class).getAllPlaces()).withSelfRel());
        return placeModel;
    }


    // Método auxiliar para obter os nomes das propriedades nulas
    private String[] getNullPropertyNames(Object source) {

        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] atributes = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor atribute : atributes) {
            Object srcValue = src.getPropertyValue(atribute.getName());
            if (srcValue == null) emptyNames.add(atribute.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public PlaceModel update(UUID id, PlaceDTO placeDto) {
        var placeModel = placeRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MESSAGE_NOT_FOUND_ENTITY));

        BeanUtils.copyProperties(placeDto, placeModel, getNullPropertyNames(placeDto));
        placeModel.setUpdateDate(LocalDate.now());

        placeRepository.save(placeModel);

        placeModel.add(linkTo(methodOn(PlaceController.class).getAllPlaces()).withSelfRel());
        return placeModel;
    }

    public void delete(UUID id) {

        var placeModel = placeRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MESSAGE_NOT_FOUND_ENTITY));

        placeRepository.delete(placeModel);
    }


}
