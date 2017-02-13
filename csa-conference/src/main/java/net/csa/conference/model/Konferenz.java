package net.csa.conference.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Konferenz {
    //@ToDo uuid als String implementieren
    @Id
    String uuid;
    String name;
    Integer zeitinterval;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uUID) {
        uuid = uUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getZeitinterval() {
        return zeitinterval;
    }

    public void setZeitinterval(Integer zeitinterval) {
        this.zeitinterval = zeitinterval;
    }

    public String formatstring() {
        String returnstring = uuid + " " + name + " " + zeitinterval;
        return returnstring;
    }
}
