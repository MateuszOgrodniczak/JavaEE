package com.microservices.authorization_service.dao;

import com.microservices.authorization_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Repository
public class UserDao extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getUserByUsername(String username) {
        String preparedStatement = "SELECT password, enabled, role FROM users WHERE username=?";
        return jdbcTemplate.queryForObject(preparedStatement, new Object[]{username}, (resultSet, i) -> new User(
                username,
                resultSet.getString("password"),
                resultSet.getBoolean("enabled"),
                resultSet.getInt("role")
        ));
    }
}
