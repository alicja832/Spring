    package com.example.demoggggg.model;

    import jakarta.persistence.*;
    import java.util.ArrayList;
    import java.util.List;

    @Entity
    public class Teacher {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(unique = true, name = "id",nullable = false)
        private int id;
        private String name;
        private String email;
        private String password;
        @OneToMany(
                cascade = CascadeType.ALL,
                orphanRemoval = true
        )
        private List<Exercise> exercises = new ArrayList<>();
        public Teacher() {}
        public void AddExc(Exercise something)
        {
            System.out.println("Excercise Id");
            System.out.println(something.getId());
            exercises.add(something);
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<Exercise> getExercises() {
            return exercises;
        }

        public void setExercises(List<Exercise> exercises) {
            this.exercises = exercises;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }
