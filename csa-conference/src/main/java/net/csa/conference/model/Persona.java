package net.csa.conference.model;

/**
 * Created by mike on 30.11.16.
 */
public abstract class Persona {
    private String name;

    public Persona(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
