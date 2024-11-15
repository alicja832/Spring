package com.example.pythonapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(name = "name", nullable = false, unique = true)
    protected String name;
    @Column(name = "email", nullable = false, updatable = false, unique = true)
    protected String email;
    @Column(name = "password", nullable = false, updatable = true,unique = false)
    protected String password;

    public UserEntity(String username, String email, String password) {
        this.name = username;
        this.email = email;
        this.password = password;
    }

    public UserEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
