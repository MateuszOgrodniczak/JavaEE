package com.jee.spring_labs.user.service;

import com.jee.spring_labs.model.User;
import com.jee.spring_labs.owner.model.Apartment;
import com.jee.spring_labs.owner.model.Bill;

public interface EntityRemovalService {

    boolean removeUser(User user);

    boolean removeBuildingById(long id, Long ownerId);

    boolean removeApartment(Apartment apartment);

    boolean removeApartmentById(long id, long ownerId);

    boolean removeBill(Bill bill);

    boolean removeBillById(long id, long ownerId);
}
