package com.nuvem.system_places_manages.domain.services;

import com.nuvem.system_places_manages.application.dtos.CityDTO;
import com.nuvem.system_places_manages.domain.repository.CityRepository;
import com.nuvem.system_places_manages.domain.repository.StateRepository;
import com.nuvem.system_places_manages.domain.entity.CityEntity;
import com.nuvem.system_places_manages.domain.entity.StateEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.nuvem.system_places_manages.infra.Util.getNullPropertyNames;

@Service
public class CityServices {

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;

    public CityServices(CityRepository cityRepository, StateRepository stateRepository) {
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
    }

    @Transactional
    public CityEntity create(CityDTO cityDTO) {
        StateEntity stateEntity = stateRepository.findByNameAndActiveTrue(cityDTO.stateName())
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado: " + cityDTO.stateName()));

        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(cityDTO.name());

        stateEntity.addCity(cityEntity);

        return cityRepository.save(cityEntity);
    }

    public CityEntity getById(UUID id) {
        return cityRepository.findByIdAndActiveTrue(id)
                .filter(CityEntity::isActive) // Filtra apenas cidades ativas
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado ou cidade não está ativa."));
    }

    public List<CityEntity> getAll() {
        return cityRepository.findByActiveTrue();
    }

    public void delete(UUID id) {
        CityEntity cityEntity = cityRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado ou cidade não está ativa."));
        cityEntity.setActive(false);
        cityRepository.save(cityEntity);
    }

    public CityEntity update(UUID id, CityDTO cityDTO) {
        CityEntity cityEntity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado."));

        BeanUtils.copyProperties(cityDTO, cityEntity, getNullPropertyNames(cityDTO));

        return cityRepository.save(cityEntity);
    }
}
