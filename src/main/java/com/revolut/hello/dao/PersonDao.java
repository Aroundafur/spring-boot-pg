package com.revolut.hello.dao;

import com.revolut.hello.model.Person;

import java.util.List;
import java.util.Optional;


public interface PersonDao {

    int insertPerson(String username, Person person);

    List<Person> selectAllPeople();

    Optional<Person> selectPersonByUsername(String username);

    int deletePersonByUsername(String username);

    int updatePersonByUsername(String username, Person person);
}
