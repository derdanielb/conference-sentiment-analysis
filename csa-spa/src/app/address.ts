export class Address {
  public street : string;
  public number : number;
  public town : string;
  public zipCode : number;
  public country : string;

  public toString() : string {
    return this.country + " " +
        this.zipCode + " " + this.town + " " +
        this.street + " " + this.number;
  }
}
