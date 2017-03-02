import {Address} from "./address";
import {Location} from "./location";

export class EventLocation {
  public name : string;
  public address : Address;
  public geoLocation : Location;

  public toString() : string {
    return this.name + ": " + this.address + " " + this.geoLocation;
  }
}
