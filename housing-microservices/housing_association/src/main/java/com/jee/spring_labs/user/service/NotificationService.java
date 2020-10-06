package com.jee.spring_labs.user.service;

import com.jee.spring_labs.user.model.Notification;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface NotificationService {
    void addNotificationToDatabase(Notification notification);
    boolean changeStatus(long id, long userId, boolean status);
}
