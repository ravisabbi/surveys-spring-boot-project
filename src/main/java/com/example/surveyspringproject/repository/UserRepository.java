package com.example.surveyspringproject.repository;

import com.example.surveyspringproject.model.UserSurvey;
import com.example.surveyspringproject.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {


   Users findByUserName(String userName);
    Users findByEmail(String email);



    Long countByActiveAndRole(boolean status, String role);

//    List<Users> findAllByRole(String role);
    List<Users> findUsersByRole(String role);


    @Query(value = "select u.id as id,u.user_name as userName," +
            "count(us.survey_id) as surveysCount" +
            " from users as u inner join user_survey as us on u.id = us.user_id where u.role = 'USER' GROUP BY u.id",nativeQuery = true)
    List<UserSurveyCount> findByUserWithSurveyCount();

    public interface UserSurveyCount {
        Long getId();
        String getUserName();
        Long getSurveysCount();
    }

}
