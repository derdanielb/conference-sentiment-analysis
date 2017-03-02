import {Conference} from "../conference";

export class ConferenceTweets extends Conference{
  public tweets : string[];

  public constructor(obj) {
    super(obj);
    this.tweets = obj.tweets;
  }
}
