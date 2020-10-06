package com.jee.spring_labs.admin.controller;

import com.jee.spring_labs.user.model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/applications")
/*@CrossOrigin(origins="*")*/
public class ApplicationsRestController {
    private static final String XML_SUFFIX = ".xml";

    @Value("${microservices.application.url}")
    private String applicationUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(produces = "application/json")
    @SuppressWarnings("unchecked")
    public List<Application> getApplicationsInJson() {
        return restTemplate.getForObject(applicationUrl, List.class);
    }

    @GetMapping(value = XML_SUFFIX, produces = "application/xml")
    @SuppressWarnings("unchecked")
    public List<Application> getApplicationsInXml() {
        return restTemplate.getForObject(applicationUrl, List.class);
    }

    @GetMapping(value = "/{title}", produces = "application/json")
    public Application getApplicationInJson(@PathVariable("title") String title) {
        return restTemplate.getForObject(applicationUrl + "/title/" + title, Application.class);
    }

    @GetMapping(value = "/{title}" + XML_SUFFIX, produces = "application/json")
    public Application getApplicationInXml(@PathVariable("title") String title) {
        return restTemplate.getForObject(applicationUrl + "/title/" + title, Application.class);
    }
}
