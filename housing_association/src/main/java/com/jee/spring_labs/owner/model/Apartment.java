package com.jee.spring_labs.owner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jee.spring_labs.model.Building;
import com.jee.spring_labs.model.User;
import com.jee.spring_labs.tenant.model.Consumption;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean removed;

    @Column(name = "room_number")
    private int roomNumber;

    @ManyToOne
    @JoinColumn(name = "fk_building")
    private Building building;

    @ManyToOne
    @JoinColumn(name = "fk_tenant")
    private User tenant;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "apartment")
    private List<Consumption> consumptions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "apartment")
    @JsonIgnore
    private List<Bill> bills = new ArrayList<>();

    public void addConsumption(Consumption consumption) {
        consumptions.add(consumption);
        consumption.setApartment(this);
    }

    public void removeConsumption(Consumption consumption) {
        consumptions.remove(consumption);
        consumption.setApartment(null);
    }

    public void addBill(Bill bill) {
        bills.add(bill);
        bill.setApartment(this);
    }

    public void removeBill(Bill bill) {
        bills.remove(bill);
        bill.setApartment(null);
    }
}
