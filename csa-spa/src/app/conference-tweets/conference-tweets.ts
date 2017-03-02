import {Conference} from "../conference";

export class ConferenceTweets extends Conference{
  public tweets : string[];

  public constructor(obj) {
    super(obj);
    JSON.parse(obj.text()).forEach(item => {
      this.tweets.push(item);
    });
  }

  public toString() : string {
    return this.tweets.toString();
  }
}
