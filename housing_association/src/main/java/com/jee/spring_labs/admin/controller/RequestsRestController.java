package com.jee.spring_labs.admin.controller;

import com.jee.spring_labs.model.Request;
import com.jee.spring_labs.user.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestsRestController {

    private static final String XML_SUFFIX = ".xml";

    @Autowired
    private RequestService service;

    @GetMapping(produces = "application/json")
    public List<Request> getRequestsInJson() {
        return service.getAllRequests();
    }

    @GetMapping(value = XML_SUFFIX, produces = "application/xml")
    public List<Request> getRequestsInXml() {
        return service.getAllRequests();
    }

    @GetMapping(value = "/{subject}", produces = "application/json")
    public Request getRequestInJson(@PathVariable("subject") String subject) {
        return service.getRequestBySubject(subject);
    }

    @GetMapping(value = "/{subject}" + XML_SUFFIX, produces = "application/json")
    public Request getRequestInXml(@PathVariable("subject") String subject) {
        return service.getRequestBySubject(subject);
    }
}
