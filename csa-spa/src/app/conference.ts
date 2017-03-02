import {UUID} from "angular2-uuid";
import {EventLocation} from "./event-location";
import {Persona} from "./persona";
import {TimeSpan} from "./time-span";

export class Conference {
  public uuid : string = UUID.UUID();
  public name : string;
  public timeSpan : TimeSpan;
  public location : EventLocation;
  public hashTag : string;
  public organisers : Persona[];
  public sponsors : Persona[];
}
