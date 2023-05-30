package com.example.surveyspringproject.web.rest;

import com.example.surveyspringproject.exception.SurveyException;
import com.example.surveyspringproject.model.UserSurvey;
import com.example.surveyspringproject.service.UserSurveyService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@CrossOrigin("*")
public class UserSurveyResource {
    @Autowired
    private UserSurveyService userSurveyService;
    @PostMapping("/sendSurvey")
    @CrossOrigin("*")
    ResponseEntity<?> sendSurvey(@RequestBody   Map<String,Object> requestBody ) throws MessagingException {
        String email = (String) requestBody.get("email");
        HashMap<String,String> resMap = new HashMap<>();
        if (isValidEmail(email)) {
            try {
                String msg = userSurveyService.sendSurvey(requestBody);
                resMap.put("message", msg);
                return ResponseEntity.status(HttpStatus.OK).body(resMap);
            } catch (Exception e) {
                resMap.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resMap);
            }
        } else {
            resMap.put("message", "Invalid mail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resMap);
        }

    }

    @GetMapping("/surveys/{userId}")
    List<UserSurvey> getAllSurveys(@PathVariable("userId") Long userId){
        return userSurveyService.getAllSurveys(userId);
    }
    @GetMapping("/surveysData/{userId}")
    HashMap<String,Object> getAllSurveysData(@PathVariable("userId") Long userId){

         return userSurveyService.getAllUserSurveysData(userId);
    }
    @GetMapping("/surveyForm/{surveyId}")
    ResponseEntity<?> getSurvey(@PathVariable("surveyId") Long surveyId){
         try{
            return ResponseEntity.status(HttpStatus.OK).body(userSurveyService.getSurvey(surveyId));
         }
         catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( e.getMessage());
         }
    }

    @PatchMapping("/submitSurvey/{surveyId}")
    ResponseEntity<?> submitSurvey(@PathVariable("surveyId") Long surveyId) throws SurveyException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userSurveyService.submitSurvey(surveyId ));
        }
        catch (SurveyException s){

            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(s.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
