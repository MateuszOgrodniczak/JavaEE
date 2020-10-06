package com.jee.spring_labs.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jee.spring_labs.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "activation_token")
public class ActivationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String value;

    @Column(name = "creation_date")
    @JsonFormat(pattern = "DD/MM/YYYY HH:mm")
    private LocalDateTime creationDate;

    @Column(name = "expiration_date")
    @JsonFormat(pattern = "DD/MM/YYYY HH:mm")
    private LocalDateTime expirationDate;

    @OneToOne
    @JoinColumn(name = "fk_user")
    private User user;
}
