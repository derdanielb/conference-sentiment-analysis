package net.csa.conference.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Conference {

	@Id
	private String uuid;

	private String conferenceName;
	private String from;
	private String to;
	private String locationName;
	private String street;
	private String houseNumber;
	private String postcode;
	private String city;
	private String country;
	private GeoLocation geoLocation;
	private String twitterHashTag;
	private String[] organizerStringArray;
	private String[] sponsorStringArray;
	private List<Organizer> organizerList;
	private List<Sponsor> sponsorsList;

	public Conference() {
	}

	public Conference(String conferenceName, String from, String to, String locationName, String street, String houseNumber, String postcode, String city, String country, String twitterHashTag, String[] organizerStringArray, String[] sponsorStringArray) {
		this.conferenceName = conferenceName;
		this.from = from;
		this.to = to;
		this.locationName = locationName;
		this.street = street;
		this.houseNumber = houseNumber;
		this.postcode = postcode;
		this.city = city;
		this.country = country;
		this.twitterHashTag = twitterHashTag;
		this.geoLocation = new GeoLocation();
		this.geoLocation.setAddress(this.street + " " + this.houseNumber + ", " + this.postcode + " " + this.city + " " + this.country);
		this.organizerStringArray = organizerStringArray;
		this.sponsorStringArray = sponsorStringArray;
	}

	private List initList(String[] fillFrom) {
		if (fillFrom.length == 0) {
			return null;
		}
		List result = new ArrayList();
		for (String s : fillFrom) {
			String[] split = s.split(";");
			if (split[0].equals("Person")) {
				String firstName = split[1];
				String lastName = split[2];
				Person person = new Person(firstName, lastName);
				result.add(person);
			} else if (split[0].equals("Group")) {
				String name = split[1];
				Group group = new Group(name);
				result.add(group);
			} else if (split[0].equals("Organisation")) {
				String name = split[1];
				Organisation organisation = new Organisation(name);
				result.add(organisation);
			}
		}
		return result;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public String getLocationName() {
		return this.locationName;
	}

	public void initGeoLocation() {
		if (this.geoLocation == null) {
			this.geoLocation = new GeoLocation();
		}
		this.geoLocation.setAddress(this.street + " " + this.houseNumber + ", " + this.postcode + " " + this.city + " " + this.country);
		this.geoLocation.requestGeoLocaion();
	}

	public GeoLocation getGeoLocation() {
		return this.geoLocation;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getStreet() {
		return street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public String getPostcode() {
		return postcode;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getTwitterHashTag() {
		return twitterHashTag;
	}

	public String[] getOrganizerStringArray() {return organizerStringArray;}

	public String[] getSponsorStringArray() {return sponsorStringArray;}

	public List<Organizer> getOrganizerList() {
		if (this.organizerList == null) {
			this.organizerList = initList(this.organizerStringArray);
		}
		return this.organizerList;
	}

	public List<Sponsor> getSponsorsList() {
		if (this.sponsorsList == null) {
			this.sponsorsList = initList(this.sponsorStringArray);
		}
		return this.sponsorsList;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setTwitterHashTag(String twitterHashTag) {
		this.twitterHashTag = twitterHashTag;
	}

	public void setOrganizerStringArray(String[] organizerStringArray) {
		this.organizerStringArray = organizerStringArray;
	}

	public void setSponsorStringArray(String[] sponsorStringArray) {
		this.sponsorStringArray = sponsorStringArray;
	}

	public void setOrganizerList(List<Organizer> organizerList) {
		this.organizerList = organizerList;
	}

	public void setSponsorsList(List<Sponsor> sponsorsList) {
		this.sponsorsList = sponsorsList;
	}

	@Override
	public String toString() {
		return "Name: " + getConferenceName() + " Address: " + this.street + " " + this.houseNumber + ", " + this.postcode + " " + this.city + " " + this.country + " LocationName: " + getLocationName();
	}
}
