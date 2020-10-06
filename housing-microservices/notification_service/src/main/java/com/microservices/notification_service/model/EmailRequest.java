package com.microservices.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private String addressee;
    private String subject;
    private String text;
    private String username;
    private String from;
}
