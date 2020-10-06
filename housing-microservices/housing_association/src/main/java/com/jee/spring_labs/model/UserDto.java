package com.jee.spring_labs.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private long id;
    private String username;
    private boolean removed;
    private boolean enabled;
}
