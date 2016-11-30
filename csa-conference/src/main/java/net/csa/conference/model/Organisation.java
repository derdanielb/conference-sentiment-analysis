package net.csa.conference.model;

/**
 * Created by mike on 30.11.16.
 */
public class Organisation extends Persona {
    public Organisation(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        return Organisation.class.isAssignableFrom(obj.getClass());
    }
}
