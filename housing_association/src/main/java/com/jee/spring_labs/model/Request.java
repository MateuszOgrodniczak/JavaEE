package com.jee.spring_labs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean removed;

    private String subject;

    private String text;

    @JsonFormat(pattern = "DD/MM/YYYY HH:mm")
    @Column(name = "sending_date")
    private LocalDateTime sendingDate;

    @ManyToOne
    @JoinColumn(name = "fk_sender")
    private User sender;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "request_recipient",
            joinColumns = {@JoinColumn(name = "fk_request")},
            inverseJoinColumns = {@JoinColumn(name = "fk_recipient")})
    private Set<User> recipients = new HashSet<>();

    public void addRecipient(User user) {
        recipients.add(user);
        user.getRequestsReceived().add(this);
    }

    public void removeRecipient(User user) {
        recipients.remove(user);
        user.getRequestsReceived().remove(this);
    }
}

