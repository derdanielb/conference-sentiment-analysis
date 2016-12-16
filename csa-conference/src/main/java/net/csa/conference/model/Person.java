package net.csa.conference.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by leoneck on 15.12.16.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public abstract class Person {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person() {

    }

    public Person(String name) {
        this.name = name;
    }

}
