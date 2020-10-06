package com.bpmn.project.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivationToken {
    private long id;
    private String value;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
}