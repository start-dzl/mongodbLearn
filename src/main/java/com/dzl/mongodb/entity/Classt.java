package com.dzl.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Classt {

    @Id
    private String id;
    private String name;


    public Classt(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Classt{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}