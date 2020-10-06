package com.jee.spring_labs.user.service.impl;

import com.jee.spring_labs.model.User;
import com.jee.spring_labs.user.dao.NotificationRepository;
import com.jee.spring_labs.user.dao.UserRepository;
import com.jee.spring_labs.user.model.Notification;
import com.jee.spring_labs.user.service.NotificationService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository dao;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void addNotificationToDatabase(Notification notification) {
        User recipient = userRepository.findUserByUsername(notification.getRecipientLogin());
        if (recipient == null) {
            return;
        }
        Hibernate.initialize(recipient.getNotifications());
        recipient.addNotification(notification);
        userRepository.save(recipient);
    }

    @Override
    @Transactional
    public boolean changeStatus(long id, long userId, boolean status) {
        Optional<Notification> optionalNotification = dao.findById(id);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            if (notification.getRecipient().getId() != userId) {
                return false;
            }
            notification.setSeen(!status);
            dao.save(notification);
            return true;
        }
        return false;
    }
}
