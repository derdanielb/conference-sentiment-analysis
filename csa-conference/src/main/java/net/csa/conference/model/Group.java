package net.csa.conference.model;

import java.util.Objects;

/**
 * Created by mike on 30.11.16.
 */
public class Group extends Persona {
    public Group(String name) {
        super(name);
    }
    public Group() {}

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        return Group.class.isAssignableFrom(obj.getClass());
    }
}
