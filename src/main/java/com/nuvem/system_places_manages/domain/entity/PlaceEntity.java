package com.nuvem.system_places_manages.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Table(name="places")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PlaceEntity extends RepresentationModel<PlaceEntity> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String name;

    private String description;

    @NotNull
    private LocalDate createDate;

    private LocalDate updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    @JsonBackReference
    private DistrictEntity district;

    private boolean active = true;

    public PlaceEntity() {
    }

    public PlaceEntity(String name, String description, LocalDate createDate, LocalDate updateDate, DistrictEntity district) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.district = district;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceEntity that = (PlaceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
