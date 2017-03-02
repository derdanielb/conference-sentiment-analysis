export class Location {
  public latitude : number;
  public longitude : number;

  public constructor(obj) {
    this.latitude = obj.latitude;
    this.longitude = obj.longitude;
  }

  public toString() : string {
    return "[" + this.latitude + ":"  + this.longitude + "]";
  }
}
