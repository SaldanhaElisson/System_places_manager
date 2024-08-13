package com.nuvem.system_places_manages.domain.services;

import com.nuvem.system_places_manages.application.dtos.DistrictDTO;
import com.nuvem.system_places_manages.domain.repository.CityRepository;
import com.nuvem.system_places_manages.domain.repository.DistrictRepository;
import com.nuvem.system_places_manages.domain.entity.CityEntity;
import com.nuvem.system_places_manages.domain.entity.DistrictEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.nuvem.system_places_manages.infra.Util.getNullPropertyNames;

@Service
public class DistrictServices {

    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;

    public DistrictServices(DistrictRepository districtRepository, CityRepository cityRepository) {
        this.districtRepository = districtRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional
    public DistrictEntity create(DistrictDTO districtDTO) {

        CityEntity cityEntity = cityRepository.findByNameAndActiveTrue(districtDTO.cityName())
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada ou desativada: " + districtDTO.cityName()));

        DistrictEntity districtEntity = new DistrictEntity();
        BeanUtils.copyProperties(districtDTO, districtEntity, "cityName");
        cityEntity.addDistrict(districtEntity);

        return districtRepository.save(districtEntity);
    }

    public DistrictEntity getById(UUID id) {
        return districtRepository.findByIdAndActiveTrue(id)
                .filter(DistrictEntity::isActive) // Filtra apenas cidades ativas
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado ou District não está ativa."));
    }

    public List<DistrictEntity> getAll() {
        return districtRepository.findByActiveTrue();
    }

    public void delete(UUID id) {
        DistrictEntity districtEntity = districtRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado ou district não está ativa."));
        districtEntity.setActive(false);
        districtRepository.save(districtEntity);
    }

    public DistrictEntity update(UUID id, DistrictDTO districtDTO) {
        DistrictEntity districtEntity = districtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado."));

        BeanUtils.copyProperties(districtDTO, districtEntity, getNullPropertyNames(districtDTO));
        return districtRepository.save(districtEntity);
    }
}
