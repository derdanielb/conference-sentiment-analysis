import {GeoLocation} from './geoLocation';
import {Organizer} from "./organizer";
import {Sponsor} from "./sponsor";

export class Conference {
  constructor(public uuid: number, public conferenceName: string, public from: string, public to: string, public locationName: string, public street: string,
              public houseNumber: string, public postcode: string, public city: string, public country: string, public geoLocation: GeoLocation,
              public twitterHashTag: string, public organizerList: Organizer[], public sponsorsList: Sponsor[])
              {

  }

}
