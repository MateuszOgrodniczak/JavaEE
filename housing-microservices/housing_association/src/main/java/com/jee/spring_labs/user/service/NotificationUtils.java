package com.jee.spring_labs.user.service;

import com.jee.spring_labs.user.dao.NotificationRepository;
import com.jee.spring_labs.user.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class NotificationUtils {

    @Autowired
    private NotificationRepository repository;

    public void addUserNotificationsToModelAndView(ModelAndView mv, HttpSession session) {
        long userId = (long) session.getAttribute("userId");
        List<Notification> notifications = repository.findAllByRecipientId(userId);
        mv.addObject("notifications", notifications);
    }
}

