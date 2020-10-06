package com.jee.spring_labs.owner.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jee.spring_labs.tenant.model.Consumption;
import com.jee.spring_labs.tenant.model.ConsumptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {
    private String id;
    private boolean accepted;
    private String buildingName;
    private String issuer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfCreation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfPayment;

    private double cost;

    private List<ConsumptionDto> consumptions = new ArrayList<>();

    public static BillDto fromBill(Bill bill) {
        return new BillDto(Long.toString(bill.getId()), bill.isAccepted(), bill.getApartment().getBuilding().getName(),
                bill.getOwner().getUsername(), bill.getDateOfCreation(), bill.getDateOfPayment(), bill.getCost(), convertConsumptions(bill.getConsumptions()));
    }

    public static List<ConsumptionDto> convertConsumptions(List<Consumption> consumptions) {
        List<ConsumptionDto> consumptionDtos = new ArrayList<>();
        consumptions.forEach(consumption -> consumptionDtos.add(ConsumptionDto.fromConsumption(consumption)));
        return consumptionDtos;
    }
}
