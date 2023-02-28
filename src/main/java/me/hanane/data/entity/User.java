package me.hanane.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import lombok.*;
import me.hanane.data.Role;
import me.hanane.registration.RegistrationRequest;
import me.hanane.trigger.Trigger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
public class User extends AbstractEntity {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private String state;
    private String country;
    @JsonIgnore
    private String hashedPassword;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Trigger> triggers;

    public User(RegistrationRequest request) {
        setFirstName(request.getFirstName());
        setLastName(request.getLastName());
        setUsername(request.getUsername());
        setEmail(request.getEmail());
        setHashedPassword(request.getPassword());
        setCity(request.getCity());
        setState(request.getState());
        setCountry(request.getCountry());
        setRoles(new HashSet<>());
        setTriggers(new ArrayList<>());
    }

}
