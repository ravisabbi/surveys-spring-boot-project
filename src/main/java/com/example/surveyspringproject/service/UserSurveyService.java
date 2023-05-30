package com.example.surveyspringproject.service;

import com.example.surveyspringproject.exception.APIException;
import com.example.surveyspringproject.exception.SurveyException;
import com.example.surveyspringproject.model.UserSurvey;
import com.example.surveyspringproject.model.Users;
import com.example.surveyspringproject.repository.SurveyRepository;
import com.example.surveyspringproject.repository.UserRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserSurveyService{
   @Autowired
   private SurveyRepository surveyRepository;
   @Autowired
   private JavaMailSender javaMailSender;
   @Autowired
   private UserRepository userRepository;
@Transactional
   public String sendSurvey(Map<String,Object> requestBody) throws MessagingException {

       String  toMail = (String) requestBody.get("email");
//       Long userId = (Long) requestBody.get("userId");
       Integer userId = (Integer) requestBody.get("userId");
       Long userIdLong = userId.longValue(); // Convert Integer to Long

       System.out.println(userId);

       Users user = userRepository.findById(userIdLong).orElseThrow(() -> new APIException("User Not Found with "+userId));
       if(toMail == null){
           throw  new APIException("Please provide email");
       }

       UserSurvey userSurvey = new UserSurvey();
       userSurvey.setEmail(toMail);
      userSurvey.setUsers(user);

      UserSurvey savedSurvey =  surveyRepository.save(userSurvey);
      Long surveyId = savedSurvey.getSurveyId();

       MimeMessage message = javaMailSender.createMimeMessage();
       message.setFrom(new InternetAddress("ravisabbi7036@gmail.com"));
       message.setRecipients(Message.RecipientType.TO, InternetAddress.parse( toMail));
       message.setSubject("Survey Form");
       message.setText("Please complete your survey by using this link http://localhost:3000/surveyForm/" +surveyId);
       javaMailSender.send(message);
       System.out.println("Email sent successfully.");
       return  "Email sent successfully.";


   }



   public List<UserSurvey> getAllSurveys(Long userId){
      return surveyRepository.findAllByUsers_id(userId);
   }

   public HashMap<String, Object> getAllUserSurveysData(Long userId){
       Long completed = surveyRepository.countBySurveyStatusAndUsers_id(true,userId);
       Long  pending = surveyRepository.countBySurveyStatusAndUsers_id(false,userId);
       System.out.println( completed);
       System.out.println(pending);
       ZonedDateTime startDate = ZonedDateTime.now().minusDays(5);
       List<SurveyRepository.FiveDaysSurveyCount> result = surveyRepository.getLast5daysSurveysCount(userId,startDate);
//       HashMap<LocalDate,Long> fiveDaysMap = new HashMap<>();
//       // create data of 5 days
//       for(int i=4; i>=0;i--){
//           LocalDate currentDate = startDate.plusDays(i).toLocalDate();
//           fiveDaysMap.put(currentDate,0L);
//       }

//       for(SurveyRepository.FiveDaysSurveyCount eachItem : result){
//           fiveDaysMap.put(eachItem.getSurveyDate(),eachItem.getSurveyCount());
//       }
//       System.out.println(result.toString());
       HashMap<String,Object> resultData = new HashMap<>();
       resultData.put("no_completed", completed);
       resultData.put("no_of_pending",pending);
       resultData.put("last5daysData",result);
     return resultData;
   }

   public UserSurvey getSurvey(Long surveyId){
            UserSurvey userSurvey = surveyRepository.findById(surveyId).orElseThrow(()-> new APIException("Survey Not Found"));
            return userSurvey;
   }

    public String submitSurvey(Long surveyId)  throws SurveyException,APIException,Exception {
            UserSurvey userSurvey = getSurvey(surveyId);
            System.out.println(userSurvey.getSurveyStatus());
            if(userSurvey.getSurveyStatus()){
                throw new SurveyException("Survey Already Submitted");
            }

            userSurvey.setSurveyStatus(true);
            surveyRepository.save(userSurvey);
            return "Submitted successfully";

    }
}
