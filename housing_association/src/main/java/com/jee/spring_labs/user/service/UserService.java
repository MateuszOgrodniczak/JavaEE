package com.jee.spring_labs.user.service;

import com.jee.spring_labs.model.User;
import com.jee.spring_labs.user.model.ActivationToken;

public interface UserService {
    boolean checkIfUsernameTaken(String username);

    boolean checkIfEmailTaken(String email);

    void saveUser(User user);

    String getEmailByUsername(String username);

    boolean setUserToken(ActivationToken token, String email);

    boolean isTokenCorrect(String tokenValue, String email);
}
