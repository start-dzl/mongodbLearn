package com.dzl.mongodb.Repository;

import com.dzl.mongodb.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends PagingAndSortingRepository<Person, String>, QuerydslPredicateExecutor<Person> {
    Optional<Person> findFirstByName(String name);

    Page<Person> findAllByNameContains(String name, Pageable pageable);
}