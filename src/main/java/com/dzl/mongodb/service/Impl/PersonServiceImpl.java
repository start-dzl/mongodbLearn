package com.dzl.mongodb.service.Impl;

import com.dzl.mongodb.Repository.ClasstRepository;
import com.dzl.mongodb.Repository.PersonRepository;
import com.dzl.mongodb.entity.Classt;
import com.dzl.mongodb.entity.Person;
import com.dzl.mongodb.entity.QPerson;
import com.dzl.mongodb.service.PersonService;
import com.google.common.collect.Lists;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.gridfs.GridFsCriteria.whereFilename;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ClasstRepository classtRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    private QPerson qPerson = QPerson.person;

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

    @Override
    public Person save(Person person, List<Classt> classts) {
        person.setClasstes(classts);
        return personRepository.save(person);
    }

    @Override
    public void saveAll(List<Person> persons) {
        personRepository.saveAll(persons);
    }

    @Override
    public Page<Person> page(String name, Pageable pageable) {
        return personRepository.findAllByNameContains(name, pageable);
    }

    @Override
    public Classt saveClasst(String name) {
        Classt classt = new Classt(name);
        return classtRepository.save(classt);
    }

    @Override
    @Transactional
    public Person testTransactional(String name) {
        Classt classt = new Classt(name);
        classtRepository.save(classt);
        throw new NullPointerException();
    }

    @Override
    public void storeFileToGridFs(String path) throws FileNotFoundException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        gridFsTemplate.store(fis, "filename.txt");
    }

    @Override
    public void findFilesInGridFs(String fileName) throws IOException {
        GridFsResource resource = gridFsTemplate.getResource("filename.txt");
        InputStream stream = resource.getInputStream();
        byte[] bytes = StreamUtils.copyToByteArray(stream);
        Path path = Paths.get("E:\\", fileName);
        Files.write(path, bytes);
    }

    @Override
    public List<Person> findAllQueryDsl(String name, Integer age) {
        BooleanExpression expression = qPerson.name.contains(name)
                .or(qPerson.age.goe(age));
       return Lists.newArrayList(personRepository.findAll(expression));
    }
}
