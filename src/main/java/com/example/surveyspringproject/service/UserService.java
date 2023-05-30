package com.example.surveyspringproject.service;

import com.example.surveyspringproject.dto.SignUpDto;

import com.example.surveyspringproject.exception.APIException;
import com.example.surveyspringproject.exception.LoginFailureException;
import com.example.surveyspringproject.exception.UserAlreadyExistsException;

import com.example.surveyspringproject.model.Users;
import com.example.surveyspringproject.repository.UserRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;


//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender javaMailSender;



    public Users registerUser(SignUpDto userDto) throws  UserAlreadyExistsException{
          if(!validateUserDto(userDto)){
              throw new IllegalArgumentException("Invalid Credentials");
          }


          if(userRepository.findByEmail(userDto.getEmail())!=null){
               throw new UserAlreadyExistsException("Email already exists");

          }

          if(userRepository.findByUserName(userDto.getUserName())!=null){
             throw  new UserAlreadyExistsException("Username already exists");
          }

          Users user = new Users();

          user.setLastName(userDto.getLastName());
          user.setFirstName(userDto.getFirstName());
          user.setUserName(userDto.getUserName());
          user.setPassword(userDto.getPassword());
          user.setEmail(userDto.getEmail());

          userRepository.save(user);

          return   user;

    }

    public Users loginUser(SignUpDto userDto){
        Users user = userRepository.findByUserName(userDto.getUserName());
        System.out.println(user.toString());
        if(user == null){
            throw new LoginFailureException("Invalid Username");
        }
       else if(!user.getPassword().equals(userDto.getPassword())){
            throw  new LoginFailureException("Invalid Password");
        }
        if(!user.getActive()){
            throw new LoginFailureException("Your access removed");

        }
        return user;

    }


    public HashMap<String,Object> getAllUsersData(){
        Long activeUsers = userRepository.countByActiveAndRole(true,"USER");
        Long in_activeUsers = userRepository.countByActiveAndRole(false,"USER");
        HashMap<String,Long> statusMap = new HashMap<>();
        statusMap.put("active",activeUsers);
        statusMap.put("in_active",in_activeUsers);

        HashMap<String,Object> resultMap = new HashMap<>();
        List<Users> users = userRepository.findUsersByRole("USER");
       // List<?> usersData = userRepository.findByUserWithSurveyCount();
        //System.out.println(usersData);
        List<UserRepository.UserSurveyCount> usersCount= userRepository.findByUserWithSurveyCount();
        System.out.println(usersCount.toString());
        resultMap.put("users_count",statusMap);
        resultMap.put("users",usersCount);
        System.out.println(activeUsers);
        System.out.println(in_activeUsers);

        return  resultMap;

    }

    public List<Users> getAllUsers(){
        List<Users> users = userRepository.findUsersByRole("USER");
       // List<Users> users = userRepository.findUsersByRole("USER");
        return  users;
//        return userRepository.findByUserWithSurveyCount();
    }


    public String inviteUser(String toMail) throws MessagingException {

            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(new InternetAddress("ravisabbi7036@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse( toMail));
            message.setSubject("Congratulations");
            message.setText("please register using this link http://localhost:3000/signUp");
            javaMailSender.send(message);
            System.out.println("Email sent successfully.");
            return  "Email sent successfully.";



    }


    public Boolean validateUserDto(SignUpDto userDto){
        Boolean isValidDto = true;
        System.out.println(userDto.toString());
        if(userDto.getLastName() == null || userDto.getFirstName() == null || userDto.getUserName() == null||userDto.getPassword() == null){
            isValidDto = false;

        }
        return isValidDto;
    }


    public String changeUserAction(Users user) {
        if(user.getId() == null || user.getActive() == null){
            throw new APIException("please provide valid credentials");
        }
        Users dbUser = userRepository.findById(user.getId()).orElseThrow(() -> new APIException("User Not Found"));
        System.out.println(dbUser);
        System.out.println(user.getActive());
        dbUser.setActive(user.getActive());
        userRepository.save(dbUser);
       return "Updated successfully";
    }
}
