package com.jee.spring_labs.user.service.impl;

import com.jee.spring_labs.model.User;
import com.jee.spring_labs.user.dao.TokenRepository;
import com.jee.spring_labs.user.dao.UserRepository;
import com.jee.spring_labs.user.model.ActivationToken;
import com.jee.spring_labs.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }

    @Override
    public boolean checkIfUsernameTaken(String username) {
        return repository.findUserByUsername(username) != null;
    }

    @Override
    public boolean checkIfEmailTaken(String username) {
        return repository.findUserByEmail(username) != null;
    }

    @Override
    public void saveUser(User user) {
        repository.save(user);
    }

    @Override
    public String getEmailByUsername(String username) {
        return repository.findUserByUsername(username).getEmail();
    }

    @Override
    public boolean setUserToken(ActivationToken token, String email) {
        User user = repository.findUserByEmail(email);
        if (user.isEnabled()) {
            return false;
        }
        ActivationToken currentToken = user.getToken();
        if (currentToken != null) {
            currentToken.setValue(token.getValue());
            currentToken.setExpirationDate(token.getExpirationDate());
            user.setToken(currentToken);
        } else {
            //user.setToken(token);
            //token.setUser(user);
            user.setToken(token);
        }
        repository.save(user);
        return true;
    }

    @Override
    public boolean isTokenCorrect(String tokenValue, String email) {
        User user = repository.findUserByEmail(email);
        if (user == null) {
            return false;
        }
        if (user.getToken() == null) {
            return false;
        }
        if (!user.getToken().getValue().equals(tokenValue)) {
            return false;
        }

        ActivationToken token = user.getToken();
        if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        user.setEnabled(true);
        user.setToken(null);
        repository.save(user);
        tokenRepository.delete(token);
        return true;
    }
}
