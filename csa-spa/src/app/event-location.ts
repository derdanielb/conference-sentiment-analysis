import {Address} from "./address";
import {Location} from "./location";

export class EventLocation {
  public name : string;
  public address : Address;
  public geoLocation : Location;

  public constructor(obj : any) {
    this.name = obj.name;
    this.address = new Address(obj.address);
    this.geoLocation = new Location(obj.geoLocation);
  }

  public toString() : string {
    return this.name + ": " + this.address + " " + this.geoLocation;
  }
}
