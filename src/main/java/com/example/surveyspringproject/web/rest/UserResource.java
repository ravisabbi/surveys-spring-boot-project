package com.example.surveyspringproject.web.rest;

import com.example.surveyspringproject.dto.SignUpDto;
import com.example.surveyspringproject.dto.UserDto;
import com.example.surveyspringproject.exception.LoginFailureException;
import com.example.surveyspringproject.exception.UserAlreadyExistsException;
import com.example.surveyspringproject.model.Users;
import com.example.surveyspringproject.repository.UserRepository;
import com.example.surveyspringproject.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;

@RestController
@CrossOrigin("*")
public class UserResource {
    @Autowired
    private UserService userService;
  /*  @PostMapping("/users")
     public ResponseEntity<?>  registerUser(@RequestBody SignUpDto userDto) throws UserAlreadyExistsException,Exception {


        return ResponseEntity.status(HttpStatus.CREATED).body( userService.registerUser(userDto));
    }*/
    @PostMapping("/users")
    public ResponseEntity<?>  registerUser(@RequestBody SignUpDto userDto) throws UserAlreadyExistsException {
           try{
               return ResponseEntity.status(HttpStatus.CREATED).body( userService.registerUser(userDto));
           }
           catch (Exception e){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( e.getMessage());
           }


    }

//    @PostMapping("/login")
//    public ResponseEntity<Users> loginUser(@RequestBody SignUpDto userDto) throws LoginFailureException{
//
//        return ResponseEntity.status(HttpStatus.FOUND).body(userService.loginUser(userDto));
//    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody SignUpDto userDto) throws LoginFailureException{

        HashMap<String,Object> resMap = new HashMap<>();

        try{
            Users loggedUser = userService.loginUser(userDto);
            resMap.put("userDetails",loggedUser);
            resMap.put("JWT","JDNVFDINZnfEHRGKVNGNVJKFDBNNVNKJGVNKJF");
            System.out.println(resMap);
            return ResponseEntity.status(HttpStatus.CREATED).body(resMap);
        }
        catch(Exception e){
            resMap.put("error",e.getMessage());
            System.out.println(resMap);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resMap);


        }




    }

    @GetMapping("/usersData")
    public ResponseEntity<?> getAllUsersData(){
        return  ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsersData());
    }

  @GetMapping("/users")
 public ResponseEntity<List<Users>> getAllUsers(){

//        return userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @PostMapping("/inviteUser")
    public  ResponseEntity<?> inviteUser(@RequestBody Map<String,String> requestBody) throws MessagingException {

        String email = requestBody.get("email");
        System.out.println(email);
        HashMap<String,String> resMap = new HashMap<>();

        try {
            if (isValidEmail(email)) {
               String msg= userService.inviteUser(email);
                resMap.put("message",msg);
                return ResponseEntity.status(HttpStatus.OK).body(resMap);
            } else {
                resMap.put("message","Invalid email address");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resMap);
            }
        } catch (Exception e) {
            resMap.put("message",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resMap);
        }



    }

    @PatchMapping("/userAction")
    public  ResponseEntity<?> changeUserAction(@RequestBody Users user){

        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.changeUserAction(user));
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
