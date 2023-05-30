package com.example.surveyspringproject.repository;

import com.example.surveyspringproject.model.UserSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<UserSurvey,Long> {
    List<UserSurvey> findAllByUsers_id(Long userId);

    Long countBySurveyStatusAndUsers_id(boolean b, Long userId);

    //Long countBySurveyStatusAndUsers_id(boolean b, Long userId);
    @Query(value = "SELECT DATE(us.survey_date) AS surveyDate, COUNT(survey_id) AS surveyCount " +
            "FROM user_survey us " +
            "WHERE us.user_id = :userId AND us.survey_date >= :startDate " +
            "GROUP BY DATE(us.survey_date)",nativeQuery = true)
    List< FiveDaysSurveyCount> getLast5daysSurveysCount(Long userId, ZonedDateTime startDate);

    public interface FiveDaysSurveyCount{

        LocalDate getSurveyDate();
        Long getSurveyCount();

    }

}
