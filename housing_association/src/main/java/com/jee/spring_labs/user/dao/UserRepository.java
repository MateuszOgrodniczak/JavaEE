package com.jee.spring_labs.user.dao;

import com.jee.spring_labs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    User findUserByUsernameAndRemovedIsFalse(String username);

    User findUserById(long id);

    User findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.removed = :removed")
    List<User> filterUsersByRemoved(@Param("removed") boolean removed);

    @Query("SELECT u FROM User u WHERE u.username LIKE :username%")
    List<User> filterUsersByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username LIKE :username% AND u.removed = :removed")
    List<User> filterUsersByUsernameAndRemoved(@Param("username") String username, @Param("removed") boolean removed);

    @Query("SELECT u.username FROM User u WHERE u.username <> :adminUsername")
    List<String> getUsernames(@Param("adminUsername") String adminUsername);

    @Query("SELECT u FROM User u WHERE u.username IN :usernames")
    List<User> getUsersByUsernames(@Param("usernames") List<String> usernames);

}
