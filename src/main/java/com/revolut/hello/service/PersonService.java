package com.revolut.hello.service;

import com.revolut.hello.dao.PersonDao;
import com.revolut.hello.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonDao personDao;

    @Autowired
    public PersonService(@Qualifier("postgres") PersonDao personDao) {
        this.personDao = personDao;
    }

    public List<Person> getAllPeople() {
        return personDao.getPeople();
    }

    public int insertPerson(String username, Person person) { return personDao.addPerson(username, person); }

    public Optional<Person> getPerson(String username) {
        return personDao.getPerson(username);
    }

    public void deletePerson(String username) {
        personDao.deletePerson(username);
    }

    public void updatePerson(String username, Person person) { personDao.updatePerson(username, person); }
}
