package com.jee.spring_labs.admin.service;

import com.jee.spring_labs.model.User;

import java.util.List;

public interface AdminService {
    <T> List<T> getItems(Class<T> clazz, String filter, Boolean removed);

    <T> T getItemById(Class<T> clazz, long id);

    <T> boolean saveItem(T item);

    <T> boolean removeItem(Class<T> clazz, long id);

    <T> boolean activateItem(Class<T> clazz, long id);

    User getUserByUserName(String username);
}
