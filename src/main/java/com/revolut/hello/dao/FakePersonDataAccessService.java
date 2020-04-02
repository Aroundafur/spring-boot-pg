package com.revolut.hello.dao;

import com.revolut.hello.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {

    private final static List<Person> DB = new ArrayList<>();

    @Override
    public List<Person> getPeople() {
        return DB;
    }

    @Override
    public int addPerson(String username, Person person) {
        DB.add(new Person(username, person.getDateOfBirth()));
        return 1;
    }

    @Override
    public Optional<Person> getPerson(String username) {
        return DB
                .stream()
                .filter(person -> person.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public int deletePerson(String username) {
        Optional<Person> personOptional = getPerson(username);
        if (!personOptional.isPresent()) {
            return 0;
        }
        DB.remove(personOptional.get());
        return 1;
    }

    @Override
    public int updatePerson(String username, Person personUpdate) {
        return getPerson(username)
                .map(person -> {
                    int indexOfPersonToDelete = DB.indexOf(person);
                    if (indexOfPersonToDelete >= 0) {
                        DB.set(indexOfPersonToDelete, new Person(username, personUpdate.getDateOfBirth()));
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }
}
