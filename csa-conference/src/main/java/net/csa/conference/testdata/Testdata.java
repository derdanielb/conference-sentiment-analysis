package net.csa.conference.testdata;


import net.csa.conference.model.*;

import java.time.LocalDateTime;
import java.time.Month;

public class Testdata {

    public static final String[] names = {
            "Test Konferenz",
            "Konferenz XY",
            "Conference of Heroes",
            "DOAG",
            "Software Quality Days 2017",
            "Social Engineering",
            "MakerCon",
            "FrOSCon",
            "ABConf",
            "Javaland"
    };

    public static final LocalDateTime[] startdatetimes = {
            LocalDateTime.of(2017, Month.JANUARY, 10, 20, 30),
            LocalDateTime.of(2017, Month.FEBRUARY, 2, 10, 0),
            LocalDateTime.of(2017, Month.APRIL, 21, 11, 30),
            LocalDateTime.of(2017, Month.MAY, 14, 13, 30),
            LocalDateTime.of(2017, Month.JULY, 3, 8, 30),
            LocalDateTime.of(2017, Month.JULY, 26, 9, 0),
            LocalDateTime.of(2017, Month.AUGUST, 12, 16, 30),
            LocalDateTime.of(2017, Month.OCTOBER, 7, 18, 0),
            LocalDateTime.of(2017, Month.NOVEMBER, 30, 7, 30),
            LocalDateTime.of(2017, Month.DECEMBER, 9, 14, 15)
    };

    public static final LocalDateTime[] enddatetimes = {
            LocalDateTime.of(2017, Month.JANUARY, 12, 15, 0),
            LocalDateTime.of(2017, Month.FEBRUARY, 5, 16, 0),
            LocalDateTime.of(2017, Month.APRIL, 22, 13, 30),
            LocalDateTime.of(2017, Month.MAY, 21, 17, 0),
            LocalDateTime.of(2017, Month.JULY, 3, 22, 0),
            LocalDateTime.of(2017, Month.JULY, 30, 14, 0),
            LocalDateTime.of(2017, Month.AUGUST, 16, 12, 30),
            LocalDateTime.of(2017, Month.OCTOBER, 10, 14, 0),
            LocalDateTime.of(2017, Month.DECEMBER, 2, 14, 30),
            LocalDateTime.of(2017, Month.DECEMBER, 15, 18, 30)
    };

    public static final String[] locations = {
            "Halle 32",
            "Saal XY",
            "Ogea Theatre",
            "Square Palaca",
            "Malopot",
            "Wirtshaus",
            "Toporavov",
            "Pentagon",
            "Haafenhoos",
            "Veranstaltungshalle A"
    };

    public static final Address[] addresses = {
            new Address("Hauptstraße", "23A", "Cologne", "50667", "Germany"),
            new Address("Adenauerweg", "45", "Berlin", "ABCDE", "Germany"),
            new Address("Mainstreet", "12E", "London", "53211", "England"),
            new Address("Park Avenue", "768", "New York", "2B5A2", "USA"),
            new Address("Washington Street", "321T", "Seattle", "AAA", "USA"),
            new Address("Bergstraße", "1", "Wien", "9725A", "Austria"),
            new Address("Odaskarov", "56", "Moskau", "32899", "Russland"),
            new Address("Pennylane", "12", "Washington", "982739", "USA"),
            new Address("Riverstreete", "48", "Amsterdam", "23121", "Netherlands"),
            new Address("Hafenstraße", "89C", "Hamburg", "11111", "Germany")
    };

    public static final GeoLocation[] geolocations = {
            new GeoLocation(10.7, -2.5),
            new GeoLocation(43, 12.37),
            new GeoLocation(12.78, -6.2),
            new GeoLocation(-62.12, 12.5),
            new GeoLocation(45.6, -32.4),
            new GeoLocation(-35.9, 8.23),
            new GeoLocation(32.11, -8.29),
            new GeoLocation(-17.4, -36.1),
            new GeoLocation(-28.45, 45.3),
            new GeoLocation(1.45, 26.78)
    };
    public static final String[] hashtags = {
            "testkonf",
            "konfxy",
            "confheroes",
            "doag",
            "sqd2017",
            "seconf",
            "makercon",
            "froscon",
            "abconf",
            "javaland"
    };

    public static final AbstractOrganiserSponsor[] aos = {
            new Group("Gruppe 42"),
            new Group("Die Gönner"),
            new Group("Anonymous")   ,
            new NaturalPerson("Meyer", "Alexander"),
            new NaturalPerson("Schmitt", "Norbert"),
            new NaturalPerson("Abdul", "Mustafa"),
            new NaturalPerson("Nuntschek", "Paris"),
            new NaturalPerson("Sharmon", "Laurence"),
            new NaturalPerson("Bolton", "Tim"),
            new NaturalPerson("Korov", "Jorg"),
            new Organisation("Volkswagen"),
            new Organisation("Intel"),
            new Organisation("AMD"),
            new Organisation("ASUS"),
            new Organisation("IT LoGaTim"),
            new Organisation("Zantek")
    };

}
