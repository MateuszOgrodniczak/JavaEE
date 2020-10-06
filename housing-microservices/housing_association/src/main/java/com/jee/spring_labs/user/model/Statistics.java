package com.jee.spring_labs.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private long usersCount;
    private long adminsCount;
    private long ownersCount;
    private long tenantsCount;
    private long buildingsCount;
    private long apartmentsCount;
    private long applicationsCount;
}
