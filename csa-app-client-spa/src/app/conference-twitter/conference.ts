import {Gop} from './gop';
import {GeoLocation} from './geoLocation';

export class Conference {
  constructor(public uuid: number, public conferenceName: string, public from: string, public to: string, public locationName: string, public street: string,
              public houseNumber: string, public postcode: string, public city: string, public country: string, public geoLocation: GeoLocation,
              public twitterHashTag: string, public organizerStringArray: string[], public sponsorStringArray: string[], public organizerList: Gop[], public sponsorsList: Gop[])
              {

  }

}
