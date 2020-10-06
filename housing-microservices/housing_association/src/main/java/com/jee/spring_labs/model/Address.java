package com.jee.spring_labs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean removed;

    private String street;
    private String city;
    private String postalCode;

    @OneToOne
    @JoinColumn(name = "fk_building")
    @JsonIgnore
    private Building building;
}
