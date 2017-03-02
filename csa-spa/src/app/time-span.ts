export class TimeSpan {
  public begin : Date;
  public end : Date;

  public constructor(obj) {
    this.begin = new Date(obj.begin);
    this.end = new Date(obj.end);
  }

  public toString() : string {
    return "[" + this.begin + "->" + this.end + "]";
  }
}
