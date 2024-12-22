package com.example.pythonapp.repository;

import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByName(String name);
    @Transactional
    @Modifying
    @Query(value = "update users set password=:password where email=:email", nativeQuery = true)
    void updateByEmail(String email,String password);


}
