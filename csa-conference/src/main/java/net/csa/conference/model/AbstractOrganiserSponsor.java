package net.csa.conference.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NaturalPerson.class, name = "naturalperson"),
        @JsonSubTypes.Type(value = Group.class, name = "group"),
        @JsonSubTypes.Type(value = Organisation.class, name = "organisation")
})
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
