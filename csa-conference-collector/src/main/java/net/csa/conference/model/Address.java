package net.csa.conference.model;

import java.util.Objects;

/**
 * Created by Felix on 30.11.2016.
 */
public class Address {
    private String street;
    private int number;
    private String town;
    private int zipCode;
    private String country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static Address fromCSVString(String address){
        Address a = new Address();

        String[] split = address.split(",");
        if(split.length != 5)
            return a;

        a.setStreet(split[0].trim());
        a.setNumber(Integer.parseInt(split[1].trim()));
        a.setTown(split[2].trim());
        a.setZipCode(Integer.parseInt(split[3].trim()));
        a.setCountry(split[4].trim());

        return a;
    }

    @Override
    public boolean equals(Object obj) {
        if(!Address.class.isAssignableFrom(obj.getClass()))
            return false;
        Address o = (Address)obj;
        return Objects.equals(street, o.street) &&
                Objects.equals(number, o.number) &&
                Objects.equals(town, o.town) &&
                Objects.equals(zipCode, o.zipCode) &&
                Objects.equals(country, o.country);
    }
}
