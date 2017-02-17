"use strict";
var Conference = (function () {
    function Conference(uuid, conferenceName, from, to, locationName, street, houseNumber, postcode, city, country, geoLocation, twitterHashTag, organizerList, sponsorsList) {
        this.uuid = uuid;
        this.conferenceName = conferenceName;
        this.from = from;
        this.to = to;
        this.locationName = locationName;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.geoLocation = geoLocation;
        this.twitterHashTag = twitterHashTag;
        this.organizerList = organizerList;
        this.sponsorsList = sponsorsList;
    }
    return Conference;
}());
exports.Conference = Conference;
