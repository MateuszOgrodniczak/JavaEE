package com.jee.spring_labs.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailRequest {
    private String addressee;
    private String subject;
    private String text;
    private String username;
}
