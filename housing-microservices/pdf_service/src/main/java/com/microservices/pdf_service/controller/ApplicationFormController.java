package com.microservices.pdf_service.controller;

import com.microservices.pdf_service.dao.ApplicationRepository;
import com.microservices.pdf_service.exception.ApplicationFormNotFoundException;
import com.microservices.pdf_service.model.Application;
import com.microservices.pdf_service.model.Notification;
import com.microservices.pdf_service.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/application")
/*@CrossOrigin("*")*/
public class ApplicationFormController {

    @Autowired
    private ApplicationRepository dao;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/title/{title}")
    public ResponseEntity<Application> getApplicationByTitle(@PathVariable String title) {
        return ResponseEntity.ok().body(dao.findByTitle(title));
    }

    @GetMapping
    public ResponseEntity<List> getAllApplicationForms() {
        List<Application> applicationForms = dao.findAll();
        return ResponseEntity.ok().body(applicationForms);
    }

    @GetMapping("/{id:.+}")
    public ResponseEntity<Application> getApplicationFormById(@PathVariable String id) {
        Application applicationForm = dao.findById(id).orElse(new Application());
        return ResponseEntity.ok().body(applicationForm);
    }

    @GetMapping(value = "/sent/{userName}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List> getSentApplicationForms(@PathVariable String userName) {
        List<Application> applicationForms = dao.findAllByIssuerLogin(userName);
        return ResponseEntity.ok().body(applicationForms);
    }

    @GetMapping(value = "/received/{userName}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List> getReceivedApplicationForms(@PathVariable String userName) {
        List<Application> applicationForms = dao.findAllByRecipientsContains(userName);
        return ResponseEntity.ok().body(applicationForms);
    }

    @PostMapping(value = "/add", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Application> addApplicationForm(@RequestBody Application application, Locale locale, HttpServletRequest request) {
        for (String recipient : application.getRecipients()) {
            Notification notification = new Notification();
            notification.setSeen(false);
            notification.setRecipientLogin(recipient);
            notification.setSentAt(LocalDateTime.now());
            String issuer = application.getIssuerNameAndSurname() + " (" + application.getIssuerLogin() + ")";
            notification.setText(messageSource.getMessage("msg.application.received", new Object[]{issuer}, locale));

            String authorizationToken = request.getHeader("Authorization");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorizationToken);
            HttpEntity<Notification> entity = new HttpEntity<>(notification, headers);

            restTemplate.postForEntity("http://localhost:8000/notification", entity, Notification.class);
        }
        Application savedApplication;
        try {
            savedApplication = dao.save(application);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(savedApplication);
    }

/*    @DeleteMapping("/delete/{id}")
    public void deleteApplicationForm(@PathVariable String id) {
        dao.deleteById(id);
    }*/

    @GetMapping("/{id}/export")
    public void exportApplicationForm(@PathVariable String id, HttpServletResponse response, Locale locale) throws Exception {
        Optional<Application> applicationOptional = dao.findById(id);
        if (applicationOptional.isPresent()) {
            Application applicationForm = applicationOptional.get();
            pdfService.exportApplicationForm(applicationForm, response, locale);
            return;
        }
        throw new ApplicationFormNotFoundException();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getApplicationsCount() {
        return ResponseEntity.ok().body(dao.count());
    }
}
