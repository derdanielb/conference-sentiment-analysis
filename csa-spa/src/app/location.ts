export class Location {
  public latitude : number;
  public longitude : number;

  public constructor(obj : any) {
    this.latitude = obj.latitude;
    this.longitude = obj.longitude;
  }

  public toString() : string {
    return "[" + this.latitude + ":"  + this.longitude + "]";
  }
}
