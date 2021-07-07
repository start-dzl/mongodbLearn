package com.dzl.mongodb.entity;

import org.mongodb.morphia.annotations.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Entity
public class Person {

    @Id
    private String id;
    private String name;
    private Integer age;

    @DBRef
    private List<Classt> classtes;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public List<Classt> getClasstes() {
        return classtes;
    }

    public void setClasstes(List<Classt> classtes) {
        this.classtes = classtes;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", classtes=" + classtes +
                '}';
    }
}