package net.csa.conference.model;

import java.util.Date;
import java.util.Objects;

/**
 * Created by mike on 30.11.16.
 */
public class TimeSpan {
    private Date begin;
    private Date end;

    public TimeSpan(Date begin, Date end){
        this.begin = begin;
        this.end = end;
    }

    public TimeSpan(){}

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object obj) {
        if(!TimeSpan.class.isAssignableFrom(obj.getClass()))
            return false;
        TimeSpan o = (TimeSpan)obj;
        return Objects.equals(begin, o.begin) &&
                Objects.equals(end, o.end);
    }
}
