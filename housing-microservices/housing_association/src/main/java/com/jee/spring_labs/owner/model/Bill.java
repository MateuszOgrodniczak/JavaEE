package com.jee.spring_labs.owner.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jee.spring_labs.model.User;
import com.jee.spring_labs.tenant.model.Consumption;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean removed;
    private boolean accepted;

    @Column(name = "date_of_creation")
    @JsonFormat(pattern = "DD/MM/YYYY")
    private LocalDate dateOfCreation;

    @Column(name = "date_of_payment")
    @JsonFormat(pattern = "DD/MM/YYYY")
    private LocalDate dateOfPayment;

    private double cost;

    private String description;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "bill")
    private List<Consumption> consumptions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "fk_apartment")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "fk_owner")
    private User owner;

    public void addConsumption(Consumption consumption) {
        consumption.setBill(this);
        consumptions.add(consumption);
    }

    public void removeConsumption(Consumption consumption) {
        consumption.setBill(null);
        consumptions.remove(consumption);
    }
}
