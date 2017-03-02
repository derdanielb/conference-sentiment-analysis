export class TimeSpan {
  public begin : Date;
  public end : Date;

  public toString() : string {
    return "[" + this.begin + "->" + this.end + "]";
  }
}
