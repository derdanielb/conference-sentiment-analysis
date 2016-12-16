package net.csa.conference.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by leoneck on 15.12.16.
 */
public class TimePeriod {

    private String start;
    private String ende;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnde() {
        return ende;
    }

    public void setEnde(String ende) {
        this.ende = ende;
    }

    public TimePeriod() {
    }

    public TimePeriod(String start, String ende) {
        this.start = start;
        this.ende = ende;
    }

}
