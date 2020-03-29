package com.revolut.hello;

import com.revolut.hello.dao.FakePersonDataAccessService;
import com.revolut.hello.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FakePersonDataAccessServiceTest {

    private FakePersonDataAccessService underTest;

    @Before
    public void setUp() {
        underTest = new FakePersonDataAccessService();
    }

    @Test
    public void canPerformCrud() {
        String unameOne = "JamesBond";
        Person personOne = new Person(unameOne, "19-05-2020");


        String unameTwo = "AnnaSmith";
        Person personTwo = new Person(unameTwo, "12-06-2020");

        // When James and Anna added to db
        underTest.insertPerson(unameOne, personOne);
        underTest.insertPerson(unameTwo, personTwo);

        // Then can retrieve James by id
        assertThat(underTest.selectPersonByUsername(unameOne))
                .isPresent()
                .hasValueSatisfying(personFromDb -> assertThat(personFromDb).isEqualToComparingFieldByField(personOne));

        // ...And also Anna by id
        assertThat(underTest.selectPersonByUsername(unameTwo))
                .isPresent()
                .hasValueSatisfying(personFromDb -> assertThat(personFromDb).isEqualToComparingFieldByField(personTwo));

        // When get all people
        List<Person> people = underTest.selectAllPeople();

        // ...List should have size 2 and should have both James and Anna
        assertThat(people)
                .hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(personOne, personTwo);

        // ... An update request (James Bond name to Jake Black)
        Person personUpdate = new Person(unameOne, "20-07-2020");

        // When Update
        assertThat(underTest.updatePersonByUsername(unameOne, personUpdate)).isEqualTo(1);

        // Then when get person with idOne then should have name as James Bond > Jake Black
        assertThat(underTest.selectPersonByUsername(unameOne))
                .isPresent()
                .hasValueSatisfying(personFromDb -> assertThat(personFromDb).isEqualToComparingFieldByField(personUpdate));

        // When Delete Jake Black
        assertThat(underTest.deletePersonByUsername(unameOne)).isEqualTo(1);

        // When get personOne should be empty
        assertThat(underTest.selectPersonByUsername(unameOne)).isEmpty();

        // Finally DB should only contain only Anna Smith
        assertThat(underTest.selectAllPeople())
                .hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(personTwo);
    }

    @Test
    public void willReturn0IfNoPersonFoundToDelete() {
        // Given
        String username = "JamesBlake";

        // When
        int deleteResult = underTest.deletePersonByUsername(username);

        // Then
        assertThat(deleteResult).isEqualTo(0);
    }

    @Test
    public void willReturn0IfNoPersonFoundToUpdate() {
        // Given
        String username = "JohnDoe";
        Person person = new Person(username, "13-08-2020");

        // When
        int deleteResult = underTest.updatePersonByUsername(username, person);

        // Then
        assertThat(deleteResult).isEqualTo(0);
    }
}