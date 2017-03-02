import {UUID} from "angular2-uuid";
import {EventLocation} from "./event-location";
import {Persona} from "./persona";
import {TimeSpan} from "./time-span";
import {Person} from "./person";
import {Group} from "./group";
import {Organisation} from "./organisation";

export class Conference {
  public uuid : string = UUID.UUID();
  public name : string;
  public timeSpan : TimeSpan;
  public location : EventLocation;
  public hashTag : string;
  public organisers : Persona[];
  public sponsors : Persona[];

  public constructor(obj = null) {
    if(obj != null) {
      this.uuid = obj.uuid;
      this.name = obj.name;
    this.timeSpan = new TimeSpan(obj.timeSpan);
      this.location = new EventLocation(obj.location);
      this.hashTag = obj.hashTag;
    this.organisers = Conference.fromList(obj.organisers);
    this.sponsors = Conference.fromList(obj.sponsors);
    }
  }

  public toString() : string {
    return "Name: " + this.name +
      "\nTimeSpan: " + this.timeSpan +
      "\nLocation: " + this.location +
      "\nHashTag: " + this.hashTag +
      "\nOrganisers: " + this.organisers +
      "\nSponsors: " + this.sponsors;
  }

  public static fromList(obj) : Persona[] {
    var list : Persona[] = [];

    obj.forEach(item => {
      switch (item["@class"]){
        case "net.csa.conference.model.Person":
          list.push(new Person(item));
          break;
        case "net.csa.conference.model.Group":
         list.push(new Group(item));
         break;
         case "net.csa.conference.model.Organisation":
         list.push(new Organisation(item));
         break;
      }
    });

    return list;
  }
}
