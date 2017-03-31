import {Address} from "./address";
import {OrganiserSponsor} from "./organiser-sponsor";

export class Conference {
  constructor(
    public uuid: string,
    public name: string,
    public location : string,
    public hashtag: string,
    public startdatetime: number[],
    public enddatetime: number[],
    public address: Address,
    public geolocation: number[],
    public organisers: OrganiserSponsor[],
    public sponsors: OrganiserSponsor[]
  ) {

  }
}
