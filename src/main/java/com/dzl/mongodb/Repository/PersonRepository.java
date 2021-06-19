package com.dzl.mongodb.Repository;

import com.dzl.mongodb.entity.Person;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends PagingAndSortingRepository<Person, String> {
    Optional<Person> findFirstByName(String name);
}