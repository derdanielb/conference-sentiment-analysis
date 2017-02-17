import {Organizer} from "./organizer";
import {Sponsor} from "./sponsor";

export class Group implements Organizer, Sponsor{
  type: string = "group";
  constructor(public name: string) {

  }
}
