import {Organizer} from "./organizer";
import {Sponsor} from "./sponsor";

export class Person implements Organizer, Sponsor{
  type: string = "person";
  name: string;
  constructor(public firstName: string, public lastName: string) {
    this.name = firstName + " " + lastName;
  }
}
