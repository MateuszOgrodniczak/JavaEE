package com.jee.spring_labs.user.service;

import com.jee.spring_labs.model.Request;

import java.util.List;

public interface RequestService {
    List<Request> getSentRequests(long userId, String subject, boolean removed);

    List<Request> getReceivedRequests(long userId, String subject, boolean removed);

    /* REST SERVICE METHODS */
    Request getRequestBySubject(String subject);

    List<Request> getAllRequests();
}
