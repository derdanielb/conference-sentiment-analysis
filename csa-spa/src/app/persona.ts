import {Person} from "./person";
import {Group} from "./group";
import {Organisation} from "./organisation";

export class Persona {
  public name : string;

  public constructor(obj) {
    this.name = obj.name;
  }

  public static fromList(obj) : Persona[] {
    var list : Persona[] = [];

    JSON.parse(obj.text()).forEach(item => {
      switch (item["@class"]){
        case "Person":
          list.push(new Person(item));
          break;
        case "Group":
          list.push(new Group(item));
          break;
        case "Organisation":
          list.push(new Organisation(item));
          break;
      }
    });

    return list;
  }
  public toString() : string {
    return this.name;
  }
}
