export class Persona {
  public name : string;

  public constructor(obj) {
    this.name = obj.name;
  }

  public toString() : string {
    return this.name;
  }
}
