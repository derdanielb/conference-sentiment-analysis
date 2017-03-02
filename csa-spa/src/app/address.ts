export class Address {
  public street : string;
  public number : number;
  public town : string;
  public zipCode : number;
  public country : string;

  public constructor(obj) {
    this.street = obj.street;
    this.number = obj.number;
    this.town = obj.town;
    this.zipCode = obj.zipCode;
    this.country = obj.country;
  }

  public toString() : string {
    return this.country + " " +
        this.zipCode + " " + this.town + " " +
        this.street + " " + this.number;
  }
}
