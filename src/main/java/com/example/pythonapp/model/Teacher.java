    package com.example.pythonapp.model;

    import jakarta.persistence.*;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.List;

    @Entity
    public class Teacher extends UserEntity implements Serializable {



        @OneToMany(fetch = FetchType.EAGER)
        @JoinColumn(name = "teacher_id")
        private List<Exercise> exercises = null;

        public Teacher() {
            exercises = new ArrayList<>();
        }
        public Teacher(UserEntity userEntity)
        {
            name = userEntity.name;
            email = userEntity.email;
            password = userEntity.password;
            role = userEntity.role;

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
        public List<Exercise> getExercises() {
            return exercises;
        }

        public void setExercises(List<Exercise> exercises) {
            this.exercises = exercises;
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
