package com.example.demoggggg.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import com.example.demoggggg.model.enums.RoleEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class UserEntity{

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(name = "name", nullable = false, unique = true)
    protected String name;
    @Column(name = "email", nullable = false, updatable = false, unique = true)
    protected String email;
    @Column(name = "password", nullable = false, updatable = true)
    protected String password;
    protected RoleEnum role;

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public UserEntity(String username, String email, String password) {
        this.name = username;
        this.email = email;
        this.password = password;
    }

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
