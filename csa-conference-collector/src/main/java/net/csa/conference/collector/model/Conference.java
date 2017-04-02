package net.csa.conference.collector.model;

import net.csa.conference.collector.ConferenceCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Conference {

    private String uuid;
    private String _class = "net.csa.conference.model.Conference";
    private String name;
    private LocalDateTime startdatetime;
    private LocalDateTime enddatetime;
    private String location;
    private Address address;
    private double[] geolocation;
    private String hashtag;
    private List<AbstractOrganiserSponsor> organisers = new ArrayList<>();
    private List<AbstractOrganiserSponsor> sponsors = new ArrayList<>();

    public Conference() {}

    public Conference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartdatetime() {
        return startdatetime;
    }

    public void setStartdatetime(LocalDateTime startdatetime) {
        this.startdatetime = startdatetime;
    }

    public LocalDateTime getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(LocalDateTime enddatetime) {
        this.enddatetime = enddatetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double[] getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(double[] geolocation) {
        this.geolocation = geolocation;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getUuid() {
        return uuid;
    }

    public List<AbstractOrganiserSponsor> getOrganisers() {
        return organisers;
    }

    public List<AbstractOrganiserSponsor> getSponsors() {
        return sponsors;
    }

    public void addOrganiser(AbstractOrganiserSponsor aos) {
        organisers.add(aos);
    }

    public void addSponsor(AbstractOrganiserSponsor aos) {
        sponsors.add(aos);
    }

    public void removeOrganiser(AbstractOrganiserSponsor aos) {
        organisers.remove(aos);
    }

    public void removeSponsor(AbstractOrganiserSponsor aos) {
        sponsors.remove(aos);
    }


    public String toString() {
        return "Conference: {"
                    + "_class: " + _class + ","
                    + "name: " + name + ","
                    + "startdatetime: " + startdatetime + ","
                    + "enddatetime: " + enddatetime + ","
                    + "location: " + location + ","
                    + "adress: " + address + ","
                    + "geolocation: [" + geolocation[0] + "," + geolocation[1] + "],"
                    + "hashtag: " + hashtag + ","
                    + "organisers: " + organisers + ","
                    + "sponsors: " + sponsors +
                "}";
    }


    //parse csv string into conference object
    public static Conference createFromCSV(String csv) {
        Conference conference = new Conference();

        final Logger log = LoggerFactory.getLogger(Conference.class);

        //Split string at semicolon
        String[] splitted = csv.split(";");
        if(splitted.length != 14) {
            log.error("CSV String has not the right number of attributes");
            if(splitted.length > 0) log.error("CSV String begins with: " + splitted[0]);
            return null;
        }

        //name
        conference.setName(splitted[0]);

        //location
        conference.setLocation(splitted[1]);

        //address
        conference.setAddress(new Address(splitted[2], splitted[3], splitted[4], splitted[5], splitted[6]));

        //geoLocation
        double[] geo = {
                Double.parseDouble(splitted[7].replace(",",".")),
                Double.parseDouble(splitted[8].replace(",","."))
        };
        conference.setGeolocation(geo);

        //startDateTime
        String[] startDateString = splitted[9].split(" ");

        String[] startDate = startDateString[0].split("\\.");
        String[] startTime = new String[2];
        if(startDateString.length > 1) startTime = startDateString[1].split(":");
        else {
            startTime[0] = "0";
            startTime[1] = "0";
        }

        LocalDateTime startDateTime = LocalDateTime.of(
                Integer.parseInt(startDate[2]),
                Integer.parseInt(startDate[1]),
                Integer.parseInt(startDate[0]),
                Integer.parseInt(startTime[0]),
                Integer.parseInt(startTime[1])
        );

        conference.setStartdatetime(startDateTime);

        //endDateTime
        String[] endDateString = splitted[10].split(" ");
        String[] endDate = startDateString[0].split("\\.");
        String[] endTime = new String[2];
        if(startDateString.length > 1) endTime = endDateString[1].split(":");
        else {
            endTime[0] = "0";
            endTime[1] = "0";
        }

        LocalDateTime endDateTime = LocalDateTime.of(
                Integer.parseInt(endDate[2]),
                Integer.parseInt(endDate[1]),
                Integer.parseInt(endDate[0]),
                Integer.parseInt(endTime[0]),
                Integer.parseInt(endTime[1])
        );

        conference.setEnddatetime(endDateTime);

        //Hashtag
        conference.setHashtag(splitted[11]);

        //organisers
        String[] organisers = splitted[12].split(",");
        AbstractOrganiserSponsor aos;

        for(int i=0; i+1<organisers.length;i+=2) {
            switch(organisers[i+1]) {
                case "O":
                    //Organisation
                    aos = new Organisation(organisers[i]);
                    conference.addOrganiser(aos);
                    break;
                case "P":
                    //NaturalPerson
                    String[] names = organisers[i].split(" ");
                    aos = new NaturalPerson(names[1], names[0]);
                    conference.addOrganiser(aos);
                    break;
                case "G":
                    //Group
                    aos = new Group(organisers[i]);
                    conference.addOrganiser(aos);
                    break;
            }

        }

        //sponsors
        String[] sponsors = splitted[13].split(",");

        for(int i=0; i<sponsors.length;i+=2) {
            switch(sponsors[i+1]) {
                case "O":
                    //Organisation
                    aos = new Organisation(sponsors[i]);
                    conference.addSponsor(aos);
                    break;
                case "P":
                    //NaturalPerson
                    String[] names = sponsors[i].split(" ");
                    aos = new NaturalPerson(names[1], names[0]);
                    conference.addSponsor(aos);
                    break;
                case "G":
                    //Group
                    aos = new Group(sponsors[i]);
                    conference.addSponsor(aos);
                    break;
            }

        }

        log.info("CSV string parsed to conference: " + conference.getName());

        return conference;
    }

    public String getClassProperty() {
        return _class;
    }
}
