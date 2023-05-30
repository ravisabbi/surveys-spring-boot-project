package com.example.surveyspringproject.mapper;
import com.example.surveyspringproject.dto.UserDto;
import com.example.surveyspringproject.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

        UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

        @Mapping(source = "id", target = "id")
        @Mapping(source = "userName", target = "userName")
        UserDto toDto(Users entity);

        List<UserDto> toDtoList(List<Users> users);
}
