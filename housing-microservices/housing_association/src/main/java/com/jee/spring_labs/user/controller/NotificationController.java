package com.jee.spring_labs.user.controller;

import com.jee.spring_labs.user.model.Notification;
import com.jee.spring_labs.user.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/notification")
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationService service;

    @RabbitListener(queues = {"227941"})
    public void listen(Notification notification) {
        System.out.println("[RABBIT] received new notifcation: " + notification.getText());
        service.addNotificationToDatabase(notification);
    }

/*    @GetMapping("/changeSeenStatus/{id}/{status}")
    @ResponseBody
    public boolean changeNotificationStatus(@PathVariable long id, @PathVariable boolean status, HttpSession session) {
        long userId = (long) session.getAttribute("userId");
        return service.changeStatus(id, userId, status);
    }*/
}
