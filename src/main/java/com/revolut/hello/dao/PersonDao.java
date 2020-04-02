package com.revolut.hello.dao;


import com.revolut.hello.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonDao {

    int addPerson(String username, Person person);

    List<Person> getPeople();

    Optional<Person> getPerson(String username);

    int deletePerson(String username);

    int updatePerson(String username, Person person);

}
