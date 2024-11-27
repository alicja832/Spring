    package com.example.pythonapp.model;
    import jakarta.persistence.*;
    import java.util.ArrayList;
    import java.util.List;


    @Entity
    public class Teacher extends UserEntity {

        @OneToMany
        private List<Exercise> exercises = new ArrayList<>();

        public Teacher() {
        }
        public Teacher(UserEntity userEntity)
        {
            name = userEntity.name;
            email = userEntity.email;
            password = userEntity.password;
        }
        public void addExercise(Exercise exercise)
        {
            exercises.add(exercise);
        }
        public void removeExercise(Exercise exercise)
        {
            exercises.remove(exercise);
        }
        public void updateExercise(Exercise exercise)
        {
            exercises.set( exercises.indexOf(exercise),exercise);
        }

        public void setExercises(List<Exercise> exercises) {
            this.exercises = exercises;
        }
        public List<Exercise> getExercises() {return exercises;}

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
