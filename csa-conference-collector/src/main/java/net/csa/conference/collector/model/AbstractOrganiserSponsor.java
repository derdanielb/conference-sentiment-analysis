package net.csa.conference.collector.model;

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
    private String _class;

    public AbstractOrganiserSponsor() {}

    public AbstractOrganiserSponsor(String name, String _class) {
        this.name = name;
        this._class =_class;
    }

    public void setName(String name) { this.name = name; }

    public String getName() {
        return name;
    }

    public boolean equals(Object obj) {
        return this.getName().equals(((AbstractOrganiserSponsor) obj).getName());
    }
}
