package com.jee.spring_labs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jee.spring_labs.owner.model.Apartment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean removed;

    @Column(unique = true)
    private String name;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "building")
    @JsonIgnore
    private Address address;

    @OneToOne
    @JoinColumn(name = "fk_owner")
    private User owner;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "building")
    @JsonIgnore
    private List<Apartment> apartments = new ArrayList<>();

    public void setAddress(Address address) {
        address.setBuilding(this);
        this.address = address;
    }

    public void addApartment(Apartment apartment) {
        apartments.add(apartment);
        apartment.setBuilding(this);
    }

    public void removeApartment(Apartment apartment) {
        apartments.remove(apartment);
        apartment.setBuilding(null);
    }
}

