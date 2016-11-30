package net.csa.conference.model;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if(!Persona.class.isAssignableFrom(obj.getClass()))
            return false;
        Persona o = (Persona)obj;
        return Objects.equals(name, o.name);
    }
}
