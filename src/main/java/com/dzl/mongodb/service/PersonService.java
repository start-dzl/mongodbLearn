package com.dzl.mongodb.service;

import com.dzl.mongodb.entity.Classt;
import com.dzl.mongodb.entity.Head;
import com.dzl.mongodb.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PersonService {
    void update(String name, Integer age);

    Person findById(String id);

    void delete(String id);

    Person save(String name);

    Person save(Person person, List<Classt> classts);

    void saveAll(List<Person> persions);

    Page<Person> page(String name, Pageable pageable);

    Classt saveClasst(String name);

    Person testTransactional(String name);

    void storeFileToGridFs(String path) throws FileNotFoundException;

    void findFilesInGridFs(String fileName) throws IOException;

    List<Person> findAllQueryDsl(String name, Integer age);

    List<Person> findAllByTemplate(String name, Integer age);

    void saveMap(String str) throws IOException;

    void saveMap(List<HashMap<String, Object>> hashMaps);
    List<Map> getMap(String str) throws IOException;

    List<Map> simLookUp();

    List<Map> lookUp();


    Map<String, Object> excelShow(String fildName, String fildValue);

    List<Head> excelShowHead();

    Map<String, Head> excelMapHead();

    Head saveAndUpdate(Head head);
}
