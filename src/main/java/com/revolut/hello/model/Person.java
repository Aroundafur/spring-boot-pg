package com.revolut.hello.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class Person {

    private final String username;

    @NotBlank
    private final String dateOfBirth;

    public Person(@JsonProperty("username") String username,
                  @JsonProperty("dateOfBirth") String dateOfBirth) {
        this.username = username;
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername(){
        return username;
    }

    public String getDateOfBirth(){
        return dateOfBirth;
    }
}
