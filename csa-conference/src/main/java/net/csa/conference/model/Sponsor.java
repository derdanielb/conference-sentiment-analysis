package net.csa.conference.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author philipp.amkreutz
 */
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = Person.class, name = "person"),
		@JsonSubTypes.Type(value = Group.class, name = "group"),
		@JsonSubTypes.Type(value = Organisation.class, name = "organisation")})
public interface Sponsor {

	String getName();

}
