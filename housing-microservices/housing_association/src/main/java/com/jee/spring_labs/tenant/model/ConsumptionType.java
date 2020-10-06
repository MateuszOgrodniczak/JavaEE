package com.jee.spring_labs.tenant.model;

import lombok.Getter;
import lombok.Setter;

public enum ConsumptionType {
    ELECTRICITY("kWh", 0.5),
    GAS("m<sup>3</sup>", 1.25),
    WATER("m<sup>3</sup>", 10.75),
    WASTE("m<sup>3</sup>", 10.75),
    HEATING("m<sup>2</sup>", 55.0),
    RENOVATION("h", 50.0);

    @Getter
    @Setter
    private String unit;

    @Getter
    @Setter
    private double costPerUnit;

    ConsumptionType(String unit, double costPerUnit) {
        this.unit = unit;
        this.costPerUnit = costPerUnit;
    }
}
