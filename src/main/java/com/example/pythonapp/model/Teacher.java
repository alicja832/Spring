    package com.example.pythonapp.model;
    import jakarta.persistence.*;
    import java.util.ArrayList;
    import java.util.List;


    @Entity
    public class Teacher extends UserEntity {

    
        public Teacher() {
        }
        public Teacher(UserEntity userEntity)
        {
            name = userEntity.name;
            email = userEntity.email;
            password = userEntity.password;
        }
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            Teacher teacher = (Teacher) o;
            return this.id == teacher.id;
        }
    }
