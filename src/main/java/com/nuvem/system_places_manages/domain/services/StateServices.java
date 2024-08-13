package com.nuvem.system_places_manages.domain.services;


import com.nuvem.system_places_manages.domain.exceptions.CustomValidationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import com.nuvem.system_places_manages.application.dtos.StateDTO;
import com.nuvem.system_places_manages.domain.repository.StateRepository;
import com.nuvem.system_places_manages.domain.entity.StateEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.nuvem.system_places_manages.infra.Util.getNullPropertyNames;

@Service
public class StateServices {

    private final StateRepository stateRepository;

    public StateServices(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }


    public StateEntity create(StateDTO stateDTO) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setName(stateDTO.name());
        stateEntity.setUF(stateDTO.UF());

        return stateRepository.save(stateEntity);
    }


    public StateEntity getById(UUID id) {
        return stateRepository.findByIdAndActiveTrue(id)// Filtra apenas cidades ativas
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado ou cidade não está ativa."));
    }

    public List<StateEntity> getAll() {
        return stateRepository.findByActiveTrue();
    }

    public void delete(UUID id) {
        StateEntity stateEntity = stateRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado ou cidade não está ativa."));
        stateEntity.setActive(false);
        stateRepository.save(stateEntity);
    }

    public StateEntity update(UUID id, StateDTO stateDTO) {

        if (stateRepository.existsByUF(stateDTO.UF())) {
            BindingResult bindingResult = new BeanPropertyBindingResult(stateDTO, "stateDTO");
            bindingResult.rejectValue("UF", "error.stateDTO", "já existe: " + stateDTO.UF());
            throw new CustomValidationException(bindingResult);
        }


        if (stateRepository.existsByName(stateDTO.name())) {
            BindingResult bindingResult = new BeanPropertyBindingResult(stateDTO, "stateDTO");
            bindingResult.rejectValue("name", "error.stateDTO", "já existe: "
                    + stateDTO.UF());
            throw new CustomValidationException(bindingResult);
        }

        StateEntity stateEntity = stateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado."));


        BeanUtils.copyProperties(stateDTO, stateEntity, getNullPropertyNames(stateDTO));
        return stateRepository.save(stateEntity);
    }

}
