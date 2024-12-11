package com.example.pythonapp.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "exercise")
@Inheritance(strategy = InheritanceType.JOINED)
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id",nullable = false)
    private int id;
    @Column(unique = true, name="name")
    private String name;
    @Column(name="introduction")
    private String introduction;
    @Column(name="content")
    private String content;
    @Column(name="max_points")
    private int maxPoints;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="teacher_id", nullable=false)
    @JsonIgnore
    private Teacher teacher;
    @OneToMany
    private List<LongCorrectSolutionPart> longCorrectSolutionPart;
    @OneToMany
    private List<TestingData> testingData;


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Exercise exercise = (Exercise) o;
        return this.id == exercise.id;
    }
    public List<LongCorrectSolutionPart> getLongCorrectSolutionPart() {return this.longCorrectSolutionPart;}

    public void setLongCorrectSolutionPart(List<LongCorrectSolutionPart> longCorrectSolutionPart) {
        this.longCorrectSolutionPart = longCorrectSolutionPart;
    }
    public Exercise(){}
    public Exercise(String name,String introduction, String content,int maxPoints)
    {
        this.name = name;
        this.introduction = introduction;
        this.content = content;
        this.maxPoints = maxPoints;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    
}