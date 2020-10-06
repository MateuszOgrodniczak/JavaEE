package com.jee.spring_labs.user.controller;

import com.jee.spring_labs.admin.dao.BuildingRepository;
import com.jee.spring_labs.model.UserRole;
import com.jee.spring_labs.owner.dao.ApartmentRepository;
import com.jee.spring_labs.user.dao.UserRepository;
import com.jee.spring_labs.user.model.Statistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/statistics")
@Slf4j
/*@CrossOrigin("*")*/
public class StatisticsController {

    @Value("${microservices.application.url}")
    private String applicationUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    @SuppressWarnings("ConstantConditions")
    public Statistics getStatistics() {
        long adminsCount = userRepository.countUsersByRole(UserRole.ADMIN);
        long ownersCount = userRepository.countUsersByRole(UserRole.OWNER);
        long tenantsCount = userRepository.countUsersByRole(UserRole.TENANT);

        long buildingsCount = buildingRepository.count();
        long apartmentsCount = apartmentRepository.count();

        long applicationsCount = 0;

        ResponseEntity<Long> response = restTemplate.getForEntity(applicationUrl + "/count", Long.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            applicationsCount = response.getBody();
        } else {
            log.warn("Could not obtain the amount of applications from Pdf-Service");
        }

        long usersCount = adminsCount + ownersCount + tenantsCount;
        return new Statistics(usersCount, adminsCount, ownersCount, tenantsCount, buildingsCount, apartmentsCount, applicationsCount);
    }
}
