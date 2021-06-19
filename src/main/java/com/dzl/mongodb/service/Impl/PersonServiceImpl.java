package com.dzl.mongodb.service.Impl;

import com.dzl.mongodb.Repository.PersonRepository;
import com.dzl.mongodb.entity.Person;
import com.dzl.mongodb.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void update(String name, Integer age) {
        Optional<Person> first = personRepository.findFirstByName(name);
        if(first.isPresent()) {
            Person person = first.get();
            person.setAge(age);
            personRepository.save(person);
        }
    }

    @Override
    public Person findById(String id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }

    @Override
    public void delete(String id) {
         personRepository.deleteById(id);
    }

    @Override
    public Person save(String name) {
        Person person = new Person();
        person.setName(name);
        return personRepository.save(person);
    }
}
