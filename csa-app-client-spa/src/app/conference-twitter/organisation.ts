import {Organizer} from "./organizer";
import {Sponsor} from "./sponsor";

export class Organisation implements Organizer, Sponsor{
  type: string = "organisation";
  constructor(public name: string) {

  }
}
