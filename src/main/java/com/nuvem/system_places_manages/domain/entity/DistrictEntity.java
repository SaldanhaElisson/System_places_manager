package com.nuvem.system_places_manages.domain.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "districts")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DistrictEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity city;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "district")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonManagedReference
    private Set<PlaceEntity> places;

    public void addPlace(PlaceEntity place) {
        places.add(place);
        place.setDistrict(this);
    }

    @PrePersist
    @PreUpdate
    private void formatName() {
        this.name = this.name.toUpperCase();
    }

    public void removePlace(PlaceEntity place) {
        places.remove(place);
        place.setDistrict(null);
    }

    private boolean active = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistrictEntity that = (DistrictEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
