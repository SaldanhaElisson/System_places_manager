package com.nuvem.system_places_manages.domain.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name="cities")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CityEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private StateEntity state;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "city")
    private Set<DistrictEntity> districts = new HashSet<>();

    public void addDistrict(DistrictEntity district) {
        districts.add(district);
        district.setCity(this);
    }

    public void removeDistrict(DistrictEntity district) {
        districts.remove(district);
        district.setCity(null);
    }

    private boolean active = true;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityEntity that = (CityEntity) o;
        return Objects.equals(id, that.id);
    }

    @PrePersist
    @PreUpdate
    private void formatName() {
        this.name = this.name.toUpperCase();
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
