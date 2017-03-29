package net.csa.conference.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "_class")
public abstract class AbstractOrganiserSponsor {

    private String name;

    public AbstractOrganiserSponsor() {}

    public AbstractOrganiserSponsor(String name) {
        this.name = name;
    }

    public void setName(String name) { this.name = name; }

    public String getName() {
        return name;
    }

    public boolean equals(Object obj) {
        return this.getName().equals(((AbstractOrganiserSponsor) obj).getName());
    }
}
