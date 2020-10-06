package com.microservices.authorization_service.service;

import com.microservices.authorization_service.dao.UserDao;
import com.microservices.authorization_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao dao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dao.getUserByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
