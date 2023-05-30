package com.example.surveyspringproject.dto;

import com.example.surveyspringproject.model.UserSurvey;
import com.example.surveyspringproject.model.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private Boolean active;
    private List<UserSurvey> surveys;

    public static UserDto fromEntity(Users entity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(entity, UserDto.class);
    }

}
