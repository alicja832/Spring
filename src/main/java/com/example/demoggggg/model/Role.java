package com.example.demoggggg.model;


import jakarta.persistence.*;
import com.example.demoggggg.model.enums.RoleEnum;
@Entity
@Table(name = "role")
public class Role  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    private RoleEnum role;

    @Enumerated(EnumType.STRING)
    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
