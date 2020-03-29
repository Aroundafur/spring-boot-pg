package com.revolut.hello.dao;

import com.revolut.hello.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {
    private static List<Person> DB = new ArrayList<>();

    @Override
    public int insertPerson(String username, Person person) {
        DB.add(new Person(username, person.getDateOfBirth()));
        return 1;
    }

    @Override
    public List<Person> selectAllPeople() {
        return DB;
    }

    @Override
    public Optional<Person> selectPersonByUsername(String username) {
        return DB.stream()
                .filter(person -> person.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public int deletePersonByUsername(String username) {
        Optional<Person> personMaybe = selectPersonByUsername(username);
        if(personMaybe.isEmpty()){
            return 0;
        }
        DB.remove(personMaybe.get());
        return 1;
    }

    @Override
    public int updatePersonByUsername(String username, Person update) {
        return selectPersonByUsername(username)
                .map(person -> {
                    int indexOfPersonToUpdate = DB.indexOf(person);
                    if(indexOfPersonToUpdate >= 0){
                        DB.set(indexOfPersonToUpdate, new Person(username, update.getDateOfBirth()));
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }
}
