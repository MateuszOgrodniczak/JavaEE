package com.microservices.pdf_service.dao;

import com.microservices.pdf_service.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findAllByIssuerLogin(String userName);
    List<Application> findAllByRecipientsContains(String userName);
    Application findByTitle(String title);
}
