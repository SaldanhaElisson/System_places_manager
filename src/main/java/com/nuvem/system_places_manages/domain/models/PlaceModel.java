package com.nuvem.system_places_manages.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="places")
public class PlaceModel extends RepresentationModel<PlaceModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(unique = true)
    private String placeName;

    private String description;

    @NotNull
    private LocalDate createDate;

    private LocalDate updateDate;

    public PlaceModel() {
    }

    public PlaceModel(String placeName, String description, LocalDate createDate, LocalDate updateDate) {
        this.placeName = placeName;
        this.description = description;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }


}
