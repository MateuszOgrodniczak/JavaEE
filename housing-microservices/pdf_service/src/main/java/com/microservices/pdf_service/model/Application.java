package com.microservices.pdf_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "application")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    private String id;

    private boolean removed;

    @Indexed(unique = true)
    private String title;

    private String body;

    @Field("issuer_login")
    private String issuerLogin;

    @Field("issuer_name")
    private String issuerNameAndSurname;

    @Field("creation_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate creationDate;

    private List<String> recipients = new ArrayList<>();

    @Field("room_number")
    private int roomNumber;
}
