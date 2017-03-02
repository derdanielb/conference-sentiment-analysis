export class Persona {

  public "@class" : string;
  public name : string;

  public constructor(obj) {
    this["@class"] = obj["@class"];
    this.name = obj.name;
  }

  public toString() : string {
    return this.name;
  }
}
