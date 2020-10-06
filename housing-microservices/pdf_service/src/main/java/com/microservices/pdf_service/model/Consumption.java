package com.microservices.pdf_service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "consumption")
@Getter
@Setter
public class Consumption {
    @Id
    private String id;
    private String type;
    private String unit;
    private double amount;
    private double cost;
}
