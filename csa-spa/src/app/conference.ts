import {UUID} from "angular2-uuid";
import {EventLocation} from "./event-location";

export class Conference {
  public uuid : string = UUID.UUID();
  public name : string;
  public timeSpan : undefined;
  public location : EventLocation;
  public hashTag : string;
  public organisers : undefined[];
  public sponsors : undefined[];
}
