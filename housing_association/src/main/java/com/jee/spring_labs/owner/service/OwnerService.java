package com.jee.spring_labs.owner.service;

import com.jee.spring_labs.model.User;

import java.util.List;

public interface OwnerService {
    <T> List<T> getItems(Class<T> clazz, String filter, long ownerId);

    <T> T getItemById(Class<T> clazz, long id, long ownerId);

    <T> boolean saveItem(Class<T> clazz, T item, long ownerId);

    <T> boolean removeItem(Class<T> clazz, long id, long ownerId);

    boolean addToBill(long consumptionId, long ownerId);

    boolean isRoomNumberTaken(int roomNumber, long buildingId, long apartmentId);

    User getUserByUsername(String username);
}
