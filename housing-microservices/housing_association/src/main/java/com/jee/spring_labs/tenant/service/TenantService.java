package com.jee.spring_labs.tenant.service;

import com.jee.spring_labs.owner.model.Apartment;
import com.jee.spring_labs.tenant.model.Consumption;

import java.util.List;
import java.util.Locale;

public interface TenantService {
    <T> List<T> getItems(Class<T> clazz, long tenantId);

    <T> T getItemById(Class<T> clazz, long id, long tenantId);

    <T> boolean saveItem(Class<T> clazz, T item, long tenantId);

    boolean leaveApartment(long id, Locale locale, long tenantId);

    List<Apartment> cronGetAllApartments();

    List<Consumption> filterConsumptions(long apartmentId, String type, String month);
}
