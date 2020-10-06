package com.jee.spring_labs.tenant.model;

import com.jee.spring_labs.owner.model.Apartment;
import com.jee.spring_labs.owner.model.Bill;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean removed;

    @Enumerated(EnumType.STRING)
    private ConsumptionType type;

    private LocalDate date;

    private double amount;

    private double cost;

    @ManyToOne
    @JoinColumn(name = "fk_apartment")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "fk_bill")
    private Bill bill;
}
