package com.jee.spring_labs.tenant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsumptionDto {
    private String id;
    private String type;
    private String unit;
    private double amount;
    private double cost;

    public static ConsumptionDto fromConsumption(Consumption consumption) {
        return new ConsumptionDto(Long.toString(consumption.getId()), consumption.getType().toString(), consumption.getType().getUnit(), consumption.getAmount(), consumption.getCost());
    }
}
