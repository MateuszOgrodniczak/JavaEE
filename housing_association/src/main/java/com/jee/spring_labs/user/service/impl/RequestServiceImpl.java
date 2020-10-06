package com.jee.spring_labs.user.service.impl;

import com.jee.spring_labs.model.Request;
import com.jee.spring_labs.user.dao.RequestRepository;
import com.jee.spring_labs.user.service.RequestService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestDao;

    @Override
    public List<Request> getSentRequests(long userId, String subject, boolean removed) {
        List<Request> requests = requestDao.filterRequestsBySenderId(userId, subject, removed);
        for (Request request : requests) {
            Hibernate.initialize(request.getRecipients());
        }
        return requests;
    }

    @Override
    public List<Request> getReceivedRequests(long userId, String subject, boolean removed) {
        List<Request> requests = requestDao.filterRequestsByRecipientId(userId, subject, removed);
        for (Request request : requests) {
            Hibernate.initialize(request.getRecipients());
        }
        return requests;
    }

    @Override
    public Request getRequestBySubject(String subject) {
        Request request = requestDao.findBySubject(subject);
        Hibernate.initialize(request.getRecipients());
        return request;
    }

    @Override
    public List<Request> getAllRequests() {
        List<Request> requests = requestDao.findAll();
        for (Request request : requests) {
            Hibernate.initialize(request.getRecipients());
        }
        return requests;
    }

}
