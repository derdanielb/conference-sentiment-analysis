import {Persona} from "./persona";

export class Person extends Persona{
  public firstName : string;

  public toString() : string {
    return this.firstName + " " + super.toString();
  }
}
