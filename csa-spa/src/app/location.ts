export class Location {
  public latitude : number;
  public longitude : number;

  public toString() : string {
    return "[" + this.latitude + ":"  + this.longitude + "]";
  }
}
