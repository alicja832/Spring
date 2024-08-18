package com.example.demoggggg.repository;

import com.example.demoggggg.model.Teacher;
import com.example.demoggggg.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
    UserEntity findByName(String name);
    UserEntity findByNameAndPassword(String name, String password);
    UserEntity deleteById(long id);
    List<UserEntity> findAll();
}
