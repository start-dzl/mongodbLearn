package com.dzl.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Classt {

    @Id
    private String id;
    private String name;

    private Double num;

    public Classt(String name) {
        this.name = name;
    }

    public Classt() {
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Classt{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", num=" + num +
                '}';
    }
}