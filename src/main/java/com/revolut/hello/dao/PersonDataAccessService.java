package com.revolut.hello.dao;

import com.revolut.hello.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository("postgres")
public class PersonDataAccessService implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int addPerson(String username, Person person) {
        final String sql = "INSERT INTO person(username, dateOfBirth) VALUES (?, ?)";
        try {
            jdbcTemplate.update(
                    sql,
                    username,
                    person.getDateOfBirth()
            );
        } catch (Exception e) {
            System.out.println(e);
        }
        return 1;
    }

    @Override
    public List<Person> getPeople() {
        final String sql = "SELECT username, dateOfBirth FROM person";
        List<Person> people = jdbcTemplate.query(sql, (resultSet, i) -> {
            String username = resultSet.getString("username");
            String dateOfBirth = resultSet.getString("dateOfBirth");
            return new Person(username, dateOfBirth);
        });
        return people;
    }

    @Override
    public Optional<Person> getPerson(String username) {
        final String sql = "SELECT username, dateOfBirth FROM person WHERE username = ?";
        Person person = jdbcTemplate.queryForObject(
                sql,
                new Object[]{username}, (resultSet, i) -> {
                    String pusername = resultSet.getString("username");
                    String pdateOfBirth = resultSet.getString("dateOfBirth");
                    return new Person(pusername, pdateOfBirth);
                });
        return Optional.ofNullable(person);
    }

    @Override
    public int deletePerson(String username) {
        final String sql = "DELETE FROM person WHERE username = ?";
        jdbcTemplate.update(
                sql,
                username
        );
        return 1;
    }

    @Override
    public int updatePerson(String username, Person person) {
        final String sql = "UPDATE person set dateOfBirth = ? where username = ?";
        try {
            jdbcTemplate.update(
                    sql,
                    person.getDateOfBirth(),
                    username
            );
        } catch (Exception e) {
            System.out.println(e);
        }
        return 1;
    }

    public Map<String, Person> getPeopleMap(List<Person> list) {
        Map<String, Person> map = new HashMap<>();
        for (Person person : list) {
            map.put(person.getUsername(), person);
        }
        return map;
    }

}
