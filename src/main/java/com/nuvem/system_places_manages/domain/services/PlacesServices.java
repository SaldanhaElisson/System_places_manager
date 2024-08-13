package com.nuvem.system_places_manages.domain.services;

import com.nuvem.system_places_manages.application.responses.PlaceResponse;
import com.nuvem.system_places_manages.domain.repository.DistrictRepository;
import com.nuvem.system_places_manages.domain.repository.PlaceRepository;
import com.nuvem.system_places_manages.application.controllers.PlaceController;
import com.nuvem.system_places_manages.application.dtos.PlaceDTO;
import com.nuvem.system_places_manages.domain.entity.DistrictEntity;
import com.nuvem.system_places_manages.domain.entity.PlaceEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import static com.nuvem.system_places_manages.infra.Util.getNullPropertyNames;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.*;

@Service
public class PlacesServices {

    private final PlaceRepository placeRepository;
    private final DistrictRepository districtRepository;

    public PlacesServices(PlaceRepository placeRepository, DistrictRepository districtRepository) {
        this.placeRepository = placeRepository;
        this.districtRepository = districtRepository;
    }

    @Transactional
    public PlaceResponse create(PlaceDTO placeDto) {
        PlaceEntity placeEntity = new PlaceEntity();
        BeanUtils.copyProperties(placeDto, placeEntity);
        placeEntity.setCreateDate(LocalDate.now());

        DistrictEntity districtEntity = districtRepository.findByName(placeDto.districtName())
                .orElseThrow(() -> new EntityNotFoundException("District não encontrado: " + placeDto.districtName()));


        districtEntity.addPlace(placeEntity);

        placeEntity = placeRepository.save(placeEntity);


        return new PlaceResponse(
                placeEntity.getId(),
                placeEntity.getName(),
                placeEntity.getDescription(),
                placeEntity.getCreateDate(),
                placeEntity.getUpdateDate(),
                placeEntity.getDistrict().getName(),
                placeEntity.getDistrict().getCity().getName(),
                placeEntity.getDistrict().getCity().getState().getName(),
                linkTo(methodOn(PlaceController.class).getAllPlaces()).withSelfRel()
        );
    }

    public List<PlaceResponse> getAll() {
        List<PlaceEntity> placesEntities = placeRepository.findAll();
        List<PlaceResponse> placeResponses = new ArrayList<>();

        if (!placesEntities.isEmpty()) {
            for (PlaceEntity place : placesEntities) {
                UUID id = place.getId();
                place.add(linkTo(methodOn(PlaceController.class).getPlaceById(id.toString())).withSelfRel());
                PlaceResponse placeResponse = new PlaceResponse(
                        place.getId(),
                        place.getName(),
                        place.getDescription(),
                        place.getCreateDate(),
                        place.getUpdateDate(),
                        place.getDistrict().getName(),
                        place.getDistrict().getCity().getName(),
                        place.getDistrict().getCity().getState().getName(),
                        linkTo(methodOn(PlaceController.class).getAllPlaces()).withSelfRel()
                );

                placeResponses.add(placeResponse);
            }
        }
        placeResponses.sort(Comparator.comparing(PlaceResponse::createDate));
        return placeResponses;
    }

    public PlaceResponse getById(UUID id) {
        PlaceEntity placeEntity = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado."));


        return new PlaceResponse(
                placeEntity.getId(),
                placeEntity.getName(),
                placeEntity.getDescription(),
                placeEntity.getCreateDate(),
                placeEntity.getUpdateDate(),
                placeEntity.getDistrict().getName(),
                placeEntity.getDistrict().getCity().getName(),
                placeEntity.getDistrict().getCity().getState().getName(),
                linkTo(methodOn(PlaceController.class).getAllPlaces()).withSelfRel()
        );
    }


    public PlaceResponse update(UUID id, PlaceDTO placeDto) {
        PlaceEntity placeEntity = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado."));

        BeanUtils.copyProperties(placeDto, placeEntity, getNullPropertyNames(placeDto));
        placeEntity.setUpdateDate(LocalDate.now());

        if (placeDto.districtName() != null && !placeDto.districtName().isEmpty()) {

            placeEntity.getDistrict().removePlace(placeEntity);

            DistrictEntity districtEntity = districtRepository.findByName(placeDto.districtName())
                    .orElseThrow(() -> new EntityNotFoundException("District não encontrado: " + placeDto.districtName()));

            districtEntity.addPlace(placeEntity);
        }

        placeRepository.save(placeEntity);

        return new PlaceResponse(
                placeEntity.getId(),
                placeEntity.getName(),
                placeEntity.getDescription(),
                placeEntity.getCreateDate(),
                placeEntity.getUpdateDate(),
                placeEntity.getDistrict().getName(),
                placeEntity.getDistrict().getCity().getName(),
                placeEntity.getDistrict().getCity().getState().getName(),
                linkTo(methodOn(PlaceController.class).getAllPlaces()).withSelfRel()
        );
    }

    public void delete(UUID id) {

        var placeModel = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place não encontrado"));

        placeRepository.delete(placeModel);
    }

}
