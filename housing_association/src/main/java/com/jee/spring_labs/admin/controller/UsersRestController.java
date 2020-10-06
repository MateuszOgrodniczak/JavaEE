package com.jee.spring_labs.admin.controller;

import com.jee.spring_labs.model.User;
import com.jee.spring_labs.user.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersRestController {

    private static final String XML_SUFFIX = ".xml";

    @Autowired
    private UserRepository userDao;

    @GetMapping(produces = "application/json")
    public List<User> getUsersInJson() {
        return userDao.findAll();
    }

    @GetMapping(value = XML_SUFFIX, produces = "application/xml")
    public List<User> getUsersInXml() {
        return userDao.findAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public User getUserInJson(@PathVariable("id") long id) {
        return userDao.findUserById(id);
    }

    @GetMapping(value = "/{id}" + XML_SUFFIX, produces = "application/json")
    public User getUserInXml(@PathVariable("id") long id) {
        return userDao.findUserById(id);
    }
}
