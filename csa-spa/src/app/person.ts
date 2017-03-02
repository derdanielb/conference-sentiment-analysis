import {Persona} from "./persona";

export class Person extends Persona{
  public firstName : string;

  public constructor(obj) {
    super(obj);
    this.firstName = obj.firstName;
  }

  public toString() : string {
    return this.firstName + " " + super.toString();
  }
}
