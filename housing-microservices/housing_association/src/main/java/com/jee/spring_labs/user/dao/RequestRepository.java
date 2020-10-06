package com.jee.spring_labs.user.dao;

import com.jee.spring_labs.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT r FROM Request r WHERE r.subject LIKE " + ":subject%" + " AND r.removed = :removed AND r.sender.id = :userId ORDER BY r.sendingDate DESC")
    List<Request> filterRequestsBySenderId(@Param("userId") long userId, @Param("subject") String subject, @Param("removed") boolean removed);

    @Query(value = "SELECT r.* FROM request r, users u, request_recipient ru WHERE r.subject LIKE :subject% AND r.removed = :removed AND u.id = :userId AND ru.fk_recipient = u.id AND ru.fk_request = r.id ORDER BY r.sending_date DESC", nativeQuery = true)
    List<Request> filterRequestsByRecipientId(@Param("userId") long userId, @Param("subject") String subject, @Param("removed") boolean removed);

    @Modifying
    @Query("UPDATE Request r SET r.removed = true WHERE r.id = :id")
    void setRemoved(@Param("id") long id);

    Request findBySubject(String subject);

    Request findRequestById(long id);
}