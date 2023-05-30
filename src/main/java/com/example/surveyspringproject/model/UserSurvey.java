package com.example.surveyspringproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_survey")
public class UserSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId;
    private String email;
    private ZonedDateTime surveyDate = ZonedDateTime.now();
    private Boolean surveyStatus = false;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

}
