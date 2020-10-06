package com.jee.spring_labs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jee.spring_labs.owner.model.Apartment;
import com.jee.spring_labs.user.model.ActivationToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private boolean enabled;
    private boolean removed;

    private String name;
    private String surname;
    private String email;
    private String telephone;

    private String username;

    @JsonIgnore
    private String password;

    @Enumerated
    private UserRole role;

    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "owner")
    @JsonIgnore
    private Building building;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "tenant")
    @JsonIgnore
    private List<Apartment> apartments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "sender")
    @Column(name = "requests_sent")
    @JsonIgnore
    private List<Request> requestsSent = new ArrayList<>();

    @ManyToMany(mappedBy = "recipients")
    @JsonIgnore
    private Set<Request> requestsReceived = new HashSet<>();

    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "user")
    @JsonIgnore
    private ActivationToken token;

    public void setBuilding(Building building) {
        building.setOwner(this);
        this.building = building;
    }

    public void setToken(ActivationToken token) {
        if (token != null) {
            token.setUser(this);
        }
        this.token = token;
    }

    public void addApartment(Apartment apartment) {
        apartments.add(apartment);
        apartment.setTenant(this);
    }

    public void removeApartment(Apartment apartment) {
        apartments.remove(apartment);
        apartment.setTenant(null);
    }

    public void addSentRequest(Request request) {
        requestsSent.add(request);
        request.setSender(this);
    }

    public void removeSentRequest(Request request) {
        requestsSent.remove(request);
        request.setSender(null);
    }

    /**
     * SPRING SECURITY
     **/

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.toString()));
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
