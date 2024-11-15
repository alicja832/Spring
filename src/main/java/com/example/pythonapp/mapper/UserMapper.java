package com.example.pythonapp.mapper;
import com.example.pythonapp.dto.UserCreationDto;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.model.UserEntity;
import com.example.pythonapp.model.enums.Role;

public class UserMapper {
    public UserEntity createDto(UserCreationDto userCreationDto)
    {
        UserEntity userEntity = new UserEntity(userCreationDto.getName(),userCreationDto.getEmail(), userCreationDto.getPassword());
        if(userCreationDto.getRole()==Role.TEACHER)
        {
            return new Teacher(userEntity);
        }
        else
        {
            return new Student(userEntity);
        }
    }
}
