package com.microservices.notification_service.controller;

import com.microservices.notification_service.model.EmailRequest;
import com.microservices.notification_service.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/notification")
@Slf4j
/*@CrossOrigin("*")*/
public class NotificationServiceController {

    @Value("${microservices.rabbit.server.queue}")
    private String queue;

    @Value("${microservices.housing.activation.url}")
    private String activationTokenUrl;

    @Value("${microservices.housing.activation.sender}")
    private String activationTokenSender;

    @Value("${microservices.allowed.ip}")
    private String allowedIPs;

/*    @Value("${microservices.allowed.port}")
    private int allowedPort;*/

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @PostMapping
    public ResponseEntity<String> publishNotification(@RequestBody Notification notification) {
        template.convertAndSend(queue, notification);
        log.info("Notification sent to queue: " + queue);
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/sendActivationLink")
    public ResponseEntity<String> sendActivationLink(@RequestBody EmailRequest emailRequest, HttpServletRequest servletRequest, Locale locale) {
        if (isAddressUnknown(servletRequest)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unknown incoming IP address");
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRequest.getAddressee());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getText());
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Error while sending activation link", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        Notification notification = new Notification();
        notification.setSeen(false);
        notification.setRecipientLogin(emailRequest.getUsername());
        notification.setSentAt(LocalDateTime.now());
        notification.setText(messageSource.getMessage("msg.hello", new Object[]{emailRequest.getUsername()}, locale));
        publishNotification(notification);

        return ResponseEntity.ok().body("");
    }

    @PostMapping("/sendCustomEmail")
    public ResponseEntity<Void> sendCustomEmail(@RequestBody EmailRequest emailRequest, Locale locale) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRequest.getAddressee());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getText());
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Error while sending activation link", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Notification notification = new Notification();
        notification.setSeen(false);
        notification.setRecipientLogin(emailRequest.getUsername());
        notification.setSentAt(LocalDateTime.now());
        notification.setText(messageSource.getMessage("msg.customEmail", new Object[]{emailRequest.getUsername()}, locale));
        publishNotification(notification);

        return ResponseEntity.ok().build();
    }

    private boolean isAddressUnknown(HttpServletRequest servletRequest) {
        List<String> allowedAddresses = Arrays.asList(allowedIPs.split(","));
        String remoteAddress = servletRequest.getRemoteAddr();
        if (!allowedAddresses.contains(remoteAddress)) {
            log.warn("Notification request from unknown remote: " + remoteAddress);
            return true;
        }
        return false;
    }
}
