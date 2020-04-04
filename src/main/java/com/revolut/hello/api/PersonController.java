package com.revolut.hello.api;

import com.revolut.hello.model.Person;
import com.revolut.hello.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@RequestMapping("hello")
@RestController
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(path = "{username}")
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void addPerson(@PathVariable("username") String username, @Valid @RequestBody Person personToAdd){
        if(checkStringForAllLetterUsingRegex(username) && (diff(personToAdd.getDateOfBirth()) >= 0)) {
            personService.updatePerson(username, personToAdd);
        }
    }

    @GetMapping
    public List<Person> getAllPeople(){
        return personService.getAllPeople();
    }

    @DeleteMapping(path = "{username}")
    public void deletePersonByUsername(@PathVariable("username") String username){
        personService.deletePerson(username);
    }

    @PutMapping(path = "{username}")
    public ResponseEntity<Object>  updatePerson(@PathVariable("username") String username, @Valid @RequestBody Person personToUpdate){
        HashMap<String, String> map = new HashMap<>();
        if(checkStringForAllLetterUsingRegex(username) && (diff(personToUpdate.getDateOfBirth()) >= 0)) {
            if(getPeopleMap(personService.getAllPeople()).containsKey(username)){
                personService.updatePerson(username, personToUpdate);
            }else {
                personService.insertPerson(username, personToUpdate);
            }
            return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "{username}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sayHello(@PathVariable("username") String username)
    {
        HashMap<String, String> map = new HashMap<>();
        Optional<Person> person = personService.getPerson(username);
        if(getPeopleMap(personService.getAllPeople()).containsKey(username)){
            long diff = diff(person.get().getDateOfBirth());
            map.put("messege", "Hello " + username + "! " + (diff == 0 ? "Happy birthday!" : "Your birthday is in " +
                    TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " day(s)!"));
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }



    private static boolean checkStringForAllLetterUsingRegex(String input) {
        return (Pattern.matches(".*[a-zA-Z]+.*", input));
    }

    private long diff(String date){
        Calendar cal = Calendar.getInstance();
        String currentYear = String.valueOf(cal.get(Calendar.YEAR));
        String delimiter = "-";
        String bDate[] = date.split(delimiter,3);
        bDate[0] = currentYear;
        String fmt = "yyyy-MM-dd";
        SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
        String today = myFormat.format(new Date());
        long diff = 0;
        try {
            Date date1 = myFormat.parse(bDate[0] + "-" + bDate[1] + "-" + bDate[2]);
            Date date2 = myFormat.parse(today);
            diff = date1.getTime() - date2.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    public Map<String, Person> getPeopleMap(List<Person> list) {
        Map<String, Person> map = new HashMap<>();
        for (Person person : list) {
            map.put(person.getUsername(), person);
        }
        return map;
    }



}
