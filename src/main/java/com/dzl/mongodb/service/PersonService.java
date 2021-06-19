package com.dzl.mongodb.service;

import com.dzl.mongodb.entity.Person;

public interface PersonService {
    void update(String name, Integer age);

    Person findById(String id);

    void delete(String id);

    Person save(String name);
}
