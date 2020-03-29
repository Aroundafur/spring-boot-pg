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

    public List<Person> getAllPeople(){
        return personDao.selectAllPeople();
    }

    public Optional<Person> getPersonByUsername(String username){
        return personDao.selectPersonByUsername(username);
    }

    public int deletePerson(String username){
        return personDao.deletePersonByUsername(username);
    }

    public int updatePerson(String username, Person newPerson){
        if(!personDao.selectPersonByUsername(username).isEmpty())
            return personDao.updatePersonByUsername(username, newPerson);
        else
            return personDao.insertPerson(username, newPerson);
    }
}
