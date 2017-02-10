package net.csa.conference.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Konferenz {
	//@ToDo UUID als String implementieren
	@Id
	String UUID;
	String name;
	Integer Zeitinterval;
	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getZeitinterval() {
		return Zeitinterval;
	}
	public void setZeitinterval(Integer zeitinterval) {
		Zeitinterval = zeitinterval;
	}
	
	
	
	
}
