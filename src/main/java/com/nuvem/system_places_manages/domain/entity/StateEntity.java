package com.nuvem.system_places_manages.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Data
@Entity
@Table(name = "states")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StateEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true)
    private String UF;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "state")
    private Set<CityEntity> cities = new HashSet<>();

    public void addCity(CityEntity city) {
        cities.add(city);
        city.setState(this);
    }

    public void removeCity(CityEntity city) {
        cities.remove(city);
        city.setState(null);
    }

    private boolean active = true;

    @PrePersist
    @PreUpdate
    private void formatName() {
        this.name = this.name.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateEntity that = (StateEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
