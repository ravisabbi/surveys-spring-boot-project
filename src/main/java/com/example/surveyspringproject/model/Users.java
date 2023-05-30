package com.example.surveyspringproject.model;

import com.example.surveyspringproject.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    @JsonIgnore
    private String password;
    private String role="USER";
    private Boolean active = true;
    @OneToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<UserSurvey> surveys;
}
